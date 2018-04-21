package org.faceview.user.repository;

import org.faceview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, String> {
    User getUserByUsername(String username);

    List<User> findUsersByUsernameContainingAndUsernameIsNot(String username, String loggedInUsername);

    @Query("SELECT u.friends FROM User u WHERE u.id = ?1")
    List<User> findAllFriends(String userId);
}
