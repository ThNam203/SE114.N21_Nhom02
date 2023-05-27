package com.worthybitbuilders.squadsense.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worthybitbuilders.squadsense.activities.LogInActivity;
import com.worthybitbuilders.squadsense.activities.InboxActivity;
import com.worthybitbuilders.squadsense.activities.TeamActivity;
import com.worthybitbuilders.squadsense.activities.NotificationSettingActivity;
import com.worthybitbuilders.squadsense.activities.OpenProfileActivity;
import com.worthybitbuilders.squadsense.activities.SearchEverywhereActivity;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.databinding.FragmentMoreBinding;
import com.worthybitbuilders.squadsense.utils.SwitchActivity;

public class MoreFragment extends Fragment {

    private FragmentMoreBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoreBinding.inflate(getLayoutInflater());


        //set onclick of buttons here
        binding.btnNotificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNotificationSetting_showActivity();
            }
        });

        binding.btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnInbox_showActivity();
            }
        });

        binding.btnMyteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMyteam_showActivity();
            }
        });

        binding.btnSearchEverywhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchEverywhere_showActivity();
            }
        });
        binding.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProfile_showActivity();
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwitchActivity.switchToActivity(getContext(), LogInActivity.class);
                getActivity().finish();
            }
        });
        return binding.getRoot();
    }
// define function here
    private void btnSearchEverywhere_showActivity() {
        SwitchActivity.switchToActivity(getContext(), SearchEverywhereActivity.class);
    }

    private void btnProfile_showActivity() {
        SwitchActivity.switchToActivity(getContext(), OpenProfileActivity.class);
    }

    private void btnMyteam_showActivity() {
        SwitchActivity.switchToActivity(getContext(), TeamActivity.class);
    }

    private void btnInbox_showActivity() {
        SwitchActivity.switchToActivity(getContext(), InboxActivity.class);
    }

    private void btnNotificationSetting_showActivity() {
        SwitchActivity.switchToActivity(getContext(), NotificationSettingActivity.class);
    }

}