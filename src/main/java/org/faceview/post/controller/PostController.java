package org.faceview.post.controller;

import com.google.api.Http;
import org.faceview.post.model.CreatePostBindingModel;
import org.faceview.post.model.EditPostBindingModel;
import org.faceview.post.model.GetPostViewModel;
import org.faceview.post.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/posts/new")
    public ResponseEntity cretePostAction(@RequestParam("image")MultipartFile file,
                                          @RequestParam("content") String content,
                                          Principal principal){
        CreatePostBindingModel bindingModel = new CreatePostBindingModel();
        bindingModel.setContent(content);
        bindingModel.setImage(file);

        this.postService.createPost(bindingModel, principal.getName());

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("posts/timeline")
    public ResponseEntity getUsersPosts(Principal principal){
        return new ResponseEntity<List>(this.postService.getPosts(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/posts/all")
    public ResponseEntity globalTimeline(Principal principal, @PageableDefault(size = 3) Pageable pageable){
        List<GetPostViewModel> allPosts = this.postService.getAllPosts(principal.getName(), pageable);

        return new ResponseEntity<List>(allPosts, HttpStatus.OK);
    }

    @GetMapping("/posts/all/{id}")
    public ResponseEntity friendPosts(@PathVariable String id, Principal principal){

        return new ResponseEntity<List>(this.postService.getFriendPosts(principal.getName(), id), HttpStatus.OK);
    }

    @GetMapping("/posts/remove/{id}")
    public String removePost(@PathVariable("id") String id, Principal principal){
        this.postService.removePost(principal.getName(), id);

        return null;
    }

    @PostMapping("/posts/edit")
    public ResponseEntity editPost(@RequestBody EditPostBindingModel bindingModel, Principal principal){
        this.postService.editPost(principal.getName(), bindingModel);

        return new ResponseEntity(HttpStatus.OK);
    }
}
