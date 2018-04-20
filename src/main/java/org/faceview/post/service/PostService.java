package org.faceview.post.service;

import org.faceview.post.model.CreatePostBindingModel;
import org.faceview.post.model.EditPostBindingModel;
import org.faceview.post.model.GetPostViewModel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    void createPost(CreatePostBindingModel bindingModel, String username);

    List<GetPostViewModel> getPosts(String username);

    List<GetPostViewModel> getFriendPosts(String username, String friendsId);

    List<GetPostViewModel> getAllPosts(String username, Pageable pageable);

    void removePost(String username, String postId);

    void editPost(String username, EditPostBindingModel bindingModel);
}
