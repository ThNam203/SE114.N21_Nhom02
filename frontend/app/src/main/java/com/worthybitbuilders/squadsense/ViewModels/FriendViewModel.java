package com.worthybitbuilders.squadsense.ViewModels;


import android.util.Patterns;

import androidx.lifecycle.ViewModel;


import com.google.gson.Gson;
import com.worthybitbuilders.squadsense.Models.ErrorResponse;
import com.worthybitbuilders.squadsense.Models.UserModel;
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

    public FriendViewModel() {}

    public boolean IsValidEmail(String email)
    {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void checkUserByEmail (String reveiverEmail, FriendRequestCallback callback) {
        Call<UserModel> user = friendService.getUserByEmail(reveiverEmail);
        user.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    UserModel result = response.body();
                    callback.onSuccess(result);
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
            public void onFailure(Call<UserModel> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public interface FriendRequestCallback {
        public void onSuccess(UserModel user);
        public void onFailure(String message);
    }
}
