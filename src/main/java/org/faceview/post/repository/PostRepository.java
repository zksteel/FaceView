package org.faceview.post.repository;

import org.faceview.post.entity.Post;
import org.faceview.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findAllByAuthor(User author);

    @Query("SELECT p FROM Post p JOIN p.author a WHERE p.author = ?1 OR p.author IN (?2) ORDER BY p.createdOn DESC ")
    List<Post> findAllPageable(User user, List<User> friends, Pageable pageable);
}
