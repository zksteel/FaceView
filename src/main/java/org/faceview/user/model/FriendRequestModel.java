package org.faceview.user.model;

import javax.validation.constraints.NotEmpty;

public class FriendRequestModel {

    @NotEmpty
    private String senderId;

    @NotEmpty
    private String receiverId;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
