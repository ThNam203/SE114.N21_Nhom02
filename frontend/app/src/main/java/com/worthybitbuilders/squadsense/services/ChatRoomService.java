package com.worthybitbuilders.squadsense.services;

import com.worthybitbuilders.squadsense.models.ChatMessage;
import com.worthybitbuilders.squadsense.models.ChatRoom;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatRoomService {
    @GET("{userId}/chatroom")
    Call<List<ChatRoom>> getAllChatRoom(@Path("userId") String userId);

    @GET("{userId}/chatroom/{chatRoomId}")
    Call<ChatRoom> getAChatRoom(@Path("userId") String userId, @Path("chatRoomId") String chatRoomId);

    @GET("{userId}/chatroom/{chatRoomId}/message")
    Call<List<ChatMessage>> getAllMessageInChatRoom(@Path("userId") String userId, @Path("chatRoomId") String chatRoomId);

    @POST("{userId}/chatroom")
    Call<ChatRoom> createNewChatRoom(@Path("userId") String userId, @Body JSONObject data);

    @PATCH("{userId}/chatroom/{chatRoomId}/member")
    Call<Void> addMemberToGroupChat(@Path("userId") String userId, @Path("chatRoomId") String chatRoomId, @Body JSONObject data);

    @DELETE("{userId}/chatroom/{chatRoomId}/member/{memberId}")
    Call<Void> removeMemberFromGroupChat(@Path("userId") String userId, @Path("chatRoomId") String chatRoomId, @Path("memberId") String deleteMemberId);
}
