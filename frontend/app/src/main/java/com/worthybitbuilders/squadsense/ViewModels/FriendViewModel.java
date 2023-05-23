package com.worthybitbuilders.squadsense.ViewModels;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.worthybitbuilders.squadsense.Models.ErrorResponse;
import com.worthybitbuilders.squadsense.Models.FriendRequest;
import com.worthybitbuilders.squadsense.services.FriendService;
import com.worthybitbuilders.squadsense.services.RetrofitServices;
import com.worthybitbuilders.squadsense.services.UserService;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendViewModel extends ViewModel {
    private final FriendService friendService = RetrofitServices.getFriendService();
    private final UserService userService = RetrofitServices.getUserService();

    public FriendViewModel() {}

    public boolean IsValidEmail(String email)
    {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void Invite (String senderEmail, String reveiverEmail, FriendRequestCallback callback) {
        Call<String> invitation = friendService.inviteUser(new FriendRequest(senderEmail, reveiverEmail));
        invitation.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String jwtString = response.body();
                    Log.i("JWT", jwtString);
                    SharedPreferencesManager.saveData(SharedPreferencesManager.KEYS.JWT, jwtString);
                    JWT jwt = new JWT(jwtString);
                    SharedPreferencesManager.saveData(SharedPreferencesManager.KEYS.USERID, jwt.getClaim("id").asString());
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
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface FriendRequestCallback {
        public void onSuccess();
        public void onFailure(String message);
    }
}
