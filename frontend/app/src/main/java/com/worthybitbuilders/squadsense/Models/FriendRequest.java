package com.worthybitbuilders.squadsense.Models;

public class FriendRequest {
    private String senderEmail;
    private String receiverEmail;

    public FriendRequest(String senderEmail, String receiverEmail)
    {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
