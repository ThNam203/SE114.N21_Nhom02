package com.worthybitbuilders.squadsense.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.databinding.ActivityFirstScreenBinding;
import com.worthybitbuilders.squadsense.utils.ActivityUtils;

public class FirstScreenActivity extends AppCompatActivity {
    ActivityFirstScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityFirstScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(view -> {
            ActivityUtils.switchToActivity(FirstScreenActivity.this, LogInActivity.class);
        });

        binding.btnSignup.setOnClickListener(view -> {
            ActivityUtils.switchToActivity(FirstScreenActivity.this, SignUpActivity.class);
        });
    }
}