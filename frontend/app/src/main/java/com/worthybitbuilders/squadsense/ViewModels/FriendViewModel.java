package com.worthybitbuilders.squadsense.ViewModels;


import android.util.Patterns;

import androidx.lifecycle.ViewModel;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worthybitbuilders.squadsense.Models.ErrorResponse;
import com.worthybitbuilders.squadsense.Models.FriendRequest;
import com.worthybitbuilders.squadsense.Models.UserModel;
import com.worthybitbuilders.squadsense.services.FriendService;
import com.worthybitbuilders.squadsense.services.RetrofitServices;
import com.worthybitbuilders.squadsense.services.UserService;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendViewModel extends ViewModel {
    private final FriendService friendService = RetrofitServices.getFriendService();

    public FriendViewModel() {}

    public boolean IsValidEmail(String email)
    {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void requestByEmail (String senderEmail, String reveiverEmail, FriendRequestCallback callback) {
        Call<FriendRequest> result = friendService.createRequest(new FriendRequest(senderEmail, reveiverEmail));
        result.enqueue(new Callback<FriendRequest>() {
            @Override
            public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                }
                else {
                    ErrorResponse err = null;
                    try {
                        err = new Gson().fromJson(response.errorBody().string(), ErrorResponse.class);
                    } catch (IOException e) {
                        callback.onFailure("Something has gone wrong!");
                    }
                    callback.onFailure(err.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FriendRequest> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface FriendRequestCallback {
        public void onSuccess();
        public void onFailure(String message);
    }
}
