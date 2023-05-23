package com.worthybitbuilders.squadsense.services;

import com.worthybitbuilders.squadsense.Models.FriendRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FriendService {
//    @GET("{userId}")
//    Call<UserModel> getUser(@Path("userId") String userId);
//
//    @POST("{userId}")
//    Call<UserModel> updateUser(@Path("userId") String userId);
//
//    @POST("signup")
//    Call<Void> createNewUser(@Body UserModel newUser);
    @POST("invite")
    Call<String> inviteUser(@Body FriendRequest friendRequest);
}