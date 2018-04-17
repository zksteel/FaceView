package org.faceview.user.repository;

import org.faceview.user.entity.FriendRequest;
import org.faceview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {

    FriendRequest findBySenderAndReceiverAndStateIsNull(User sender, User receiver);

    FriendRequest findBySenderAndReceiver(User sender, User receiver);

    @Query(value = "SELECT r FROM FriendRequest r WHERE r.state IS NULL AND r.receiver.id = ?1")
    List<FriendRequest> findAllActiveRequests(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE FriendRequest r SET r.state = true WHERE r.sender.id = ?1 AND r.receiver.id = ?2")
    void acceptFriendRequest(String senderId, String receiverId);

    @Transactional
    @Modifying
    @Query("UPDATE FriendRequest r SET r.state = false WHERE r.sender.id = ?1 AND r.receiver.id = ?2")
    void declineFriendRequest(String senderId, String receiverId);

}
