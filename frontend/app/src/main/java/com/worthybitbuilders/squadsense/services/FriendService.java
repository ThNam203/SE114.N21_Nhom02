package com.worthybitbuilders.squadsense.services;

import com.google.gson.JsonObject;
import com.worthybitbuilders.squadsense.Models.FriendRequest;
import com.worthybitbuilders.squadsense.Models.UserModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FriendService {
    @POST("create-request")
    Call<FriendRequest> createRequest(@Body FriendRequest friendRequest);
}