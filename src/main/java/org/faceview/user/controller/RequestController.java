package org.faceview.user.controller;

import org.faceview.user.model.FriendRequestModel;
import org.faceview.user.service.FriendRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class RequestController {

    private final FriendRequestService friendRequestService;

    public RequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/requests/new")
    public String newRequestAction(@RequestBody FriendRequestModel friendRequestModel){
        this.friendRequestService.saveRequest(friendRequestModel);

        return null;
    }

    @GetMapping("/requests/check/{id}")
    public String isRequestSend(@PathVariable String id, Principal principal){

        boolean result = this.friendRequestService.isRequestSend(principal.getName(), id);

        if (result) {
            return "{ \"result\": true }";
        } else {
            return "{ \"result\": false }";
        }
    }

    @GetMapping("/requests/active")
    public ResponseEntity<List> getActiveRequests(Principal principal){
        return new ResponseEntity<>(
                this.friendRequestService.findAllActiveRequests(principal.getName()),
                HttpStatus.OK);
    }

    @GetMapping("requests/active/check")
    public String hasActiveRequests(Principal principal){

        boolean result = this.friendRequestService.findAllActiveRequests(principal.getName()).size() > 0;

        if (result) {
            return "{ \"result\": true }";
        } else {
            return "{ \"result\": false }";
        }
    }

    @PostMapping("/requests/accept")
    public ResponseEntity acceptFriendRequest(@RequestBody FriendRequestModel friendRequestModel){
        boolean result = this.friendRequestService.acceptFriendRequest(friendRequestModel.getSenderId(), friendRequestModel.getReceiverId());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/requests/decline")
    public ResponseEntity declineFriendRequest(@RequestBody FriendRequestModel friendRequestModel){
        boolean result = this.friendRequestService.declineFriendRequest(friendRequestModel.getSenderId(), friendRequestModel.getReceiverId());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
