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

    List<User> findUsersByUsernameContainingAndUsernameIsNot(String username, String id);

    @Modifying
    @Query("UPDATE User u set u.profilePic = ?1 where u.id = ?2")
    void updateProfilePic(String url, String id);

    @Modifying
    @Query("UPDATE User u set u.coverPic = ?1 where u.id = ?2")
    void updateCoverPic(String url, String id);

    @Query("SELECT u.friends FROM User u WHERE u.id = ?1")
    List<User> findAllFriends(String userId);
}
