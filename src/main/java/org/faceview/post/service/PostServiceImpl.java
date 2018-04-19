package org.faceview.post.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.faceview.exceptions.UserIllegalEditException;
import org.faceview.post.entity.Post;
import org.faceview.post.model.CreatePostBindingModel;
import org.faceview.post.model.EditPostBindingModel;
import org.faceview.post.model.GetPostViewModel;
import org.faceview.post.repository.PostRepository;
import org.faceview.user.entity.User;
import org.faceview.user.repository.UserRepository;
import org.faceview.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.faceview.config.ApplicationBeanConfig.CLOUD_STORAGE_BUCKET;


@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final Storage storage;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, Storage storage, UserService userService, ModelMapper modelMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.storage = storage;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void createPost(CreatePostBindingModel bindingModel, String username) {
        User author = this.userService.findOneByUsername(username);
        Post post = modelMapper.map(bindingModel, Post.class);
        post.setAuthor(author);
        post.setCreatedOn(new Date());

        if(bindingModel.getImage() != null){
            String postImgDir = author.getId() + "/" + "posts/" + UUID.randomUUID();
            String test = bindingModel.getImage().getOriginalFilename();
            BlobId blobId = BlobId.of(CLOUD_STORAGE_BUCKET, postImgDir);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            try {
                Blob blob = this.storage.create(blobInfo, bindingModel.getImage().getInputStream().readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String link = this.storage.get(blobId).getMediaLink();
            post.setImageUrl(link);
        }

        this.postRepository.save(post);
    }

    @Override
    public List<GetPostViewModel> getPosts(String username) {
        User user = this.userService.findOneByUsername(username);
        List<Post> posts = this.postRepository.findAllByAuthor(user);
        Type listType = new TypeToken<List<GetPostViewModel>>(){}.getType();

        List<GetPostViewModel> viewModelList = this.modelMapper.map(posts, listType);

        return viewModelList;
    }

    @Override
    public List<GetPostViewModel> getFriendPosts(String username,  String friendsId) {

        User user = this.userService.findOneByUsername(username);
        User friend = this.userService.findById(friendsId);
        if(!user.getFriends().contains(friend)) return null;

        List<Post> posts = friend.getPosts();

        Type listType = new TypeToken<List<GetPostViewModel>>(){}.getType();
        List<GetPostViewModel> allPosts = this.modelMapper.map(posts, listType);

        return allPosts;
    }

    @Override
    public List<GetPostViewModel> getAllPosts(String username) {

        User user = this.userService.findOneByUsername(username);
        List<Post> posts = user.getPosts();

        for (User friend : user.getFriends()) {
            posts.addAll(friend.getPosts());
        }

        Type listType = new TypeToken<List<GetPostViewModel>>(){}.getType();
        List<GetPostViewModel> allPosts = this.modelMapper.map(posts, listType);

        allPosts.sort((a,b) -> b.getCreatedOn().compareTo(a.getCreatedOn()));

        return allPosts;
    }

    @Override
    public void removePost(String username, String postId) {
        User user = this.userService.findOneByUsername(username);
        Post post = this.postRepository.getOne(postId);

        if(!user.getPosts().contains(post)) throw new UserIllegalEditException();

        //TODO
        user.getPosts().remove(post);
        this.userRepository.save(user);

        this.postRepository.delete(post);
    }

    @Override
    public void editPost(String username, EditPostBindingModel bindingModel) {
        User user = this.userService.findOneByUsername(username);

        Post post = this.postRepository.getOne(bindingModel.getId());

        if(!user.getPosts().contains(post)) throw new UserIllegalEditException();

        post.setContent(bindingModel.getContent());

        this.postRepository.save(post);
    }
}
