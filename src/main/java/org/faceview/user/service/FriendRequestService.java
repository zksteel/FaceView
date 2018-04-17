package org.faceview.user.service;

import org.faceview.user.model.FriendRequestModel;
import org.faceview.user.model.FriendRequestViewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FriendRequestService {
    void saveRequest(FriendRequestModel friendRequestModel);

    boolean isRequestSend(String username, String friendId);

    List<FriendRequestViewModel> findAllActiveRequests(String username);

    Boolean acceptFriendRequest(String senderId, String receiverId);

    Boolean declineFriendRequest(String senderId, String receiverId);
}
