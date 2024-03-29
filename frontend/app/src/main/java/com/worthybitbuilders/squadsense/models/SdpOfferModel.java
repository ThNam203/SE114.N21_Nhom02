package com.worthybitbuilders.squadsense.models;

public class SdpOfferModel {
    private final String chatRoomId;
    private final String sdp;
    private final String callerId;
    private final String callerName;
    private final String callerImagePath;
    private final boolean isVideoCall;

    public SdpOfferModel(String chatRoomId, String sdp, String callerId, String callerName, String callerImagePath, boolean isVideoCall) {
        this.chatRoomId = chatRoomId;
        this.sdp = sdp;
        this.callerId = callerId;
        this.callerName = callerName;
        this.callerImagePath = callerImagePath;
        this.isVideoCall = isVideoCall;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getSdp() {
        return sdp;
    }

    public String getCallerId() {
        return callerId;
    }

    public String getCallerName() {
        return callerName;
    }

    public String getCallerImagePath() {
        return callerImagePath;
    }

    public boolean getIsVideoCall() {
        return isVideoCall;
    }
}
