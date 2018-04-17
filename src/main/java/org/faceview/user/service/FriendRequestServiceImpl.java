package org.faceview.user.service;

import org.faceview.user.entity.FriendRequest;
import org.faceview.user.entity.User;
import org.faceview.user.model.FriendRequestModel;
import org.faceview.user.model.FriendRequestViewModel;
import org.faceview.user.repository.FriendRequestRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, ModelMapper modelMapper, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public void saveRequest(FriendRequestModel friendRequestModel) {
        User sender = this.userService.findById(friendRequestModel.getSenderId());
        User receiver = this.userService.findById(friendRequestModel.getReceiverId());

        FriendRequest request = new FriendRequest();

        request.setSender(sender);
        request.setReceiver(receiver);

        this.friendRequestRepository.save(request);
    }

    @Override
    public boolean isRequestSend(String username, String friendId) {

        User sender = this.userService.findOneByUsername(username);
        User receiver = this.userService.findById(friendId);

        //Check for request send, accepted or declined
        FriendRequest requestFromSender = this.friendRequestRepository.findBySenderAndReceiver(sender, receiver);
        FriendRequest requestToSender = this.friendRequestRepository.findBySenderAndReceiver(receiver, sender);

        return requestFromSender != null || requestToSender != null;
    }

    @Override
    public List<FriendRequestViewModel> findAllActiveRequests(String username) {
        User user = this.userService.findOneByUsername(username);
        List<FriendRequest> activeRequests = this.friendRequestRepository.findAllActiveRequests(user.getId());
        java.lang.reflect.Type targetListType = new TypeToken<List<FriendRequestViewModel>>() {}.getType();

        return this.modelMapper.map(activeRequests, targetListType);
    }

    @Override
    public Boolean acceptFriendRequest(String senderId, String receiverId) {
        User receiver = this.userService.findById(receiverId);
        User sender = this.userService.findById(senderId);
        if (receiver == null || sender == null) return false;

        //Check for double friend request
        FriendRequest potentialDoubleFriendRequest = this.friendRequestRepository.findBySenderAndReceiverAndStateIsNull(receiver, sender);
        if(potentialDoubleFriendRequest != null) this.friendRequestRepository.acceptFriendRequest(receiver.getId(), sender.getId());

        this.friendRequestRepository.acceptFriendRequest(sender.getId(), receiver.getId());
        this.userService.addFriend(sender, receiver);

        return true;
    }

    @Override
    public Boolean declineFriendRequest(String senderId, String receiverId) {
        User receiver = this.userService.findById(receiverId);
        User sender = this.userService.findById(senderId);
        if (receiver == null || sender == null) return false;

        //Check for double friend request
        FriendRequest potentialDoubleFriendRequest = this.friendRequestRepository.findBySenderAndReceiverAndStateIsNull(receiver, sender);
        if(potentialDoubleFriendRequest != null) this.friendRequestRepository.declineFriendRequest(receiver.getId(), sender.getId());

        this.friendRequestRepository.declineFriendRequest(sender.getId(), receiver.getId());

        return true;
    }
}
