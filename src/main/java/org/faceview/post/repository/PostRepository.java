package org.faceview.post.repository;

import org.faceview.post.entity.Post;
import org.faceview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findAllByAuthor(User author);


}
