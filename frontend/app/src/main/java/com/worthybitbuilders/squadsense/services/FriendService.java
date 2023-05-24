package com.worthybitbuilders.squadsense.services;

import com.worthybitbuilders.squadsense.Models.FriendRequest;
import com.worthybitbuilders.squadsense.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FriendService {
    @GET("user/{email}")
    Call<UserModel> getUserByEmail(@Path("email") String email);

    @POST("invite")
    Call<String> inviteUser(@Body FriendRequest friendRequest);
}