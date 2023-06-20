package com.worthybitbuilders.squadsense.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.adapters.FriendItemAdapter;
import com.worthybitbuilders.squadsense.adapters.MemberAdapter;
import com.worthybitbuilders.squadsense.databinding.ActivityMemberBinding;
import com.worthybitbuilders.squadsense.databinding.AddMemberMoreOptionsBinding;
import com.worthybitbuilders.squadsense.databinding.ProjectMoreOptionsBinding;
import com.worthybitbuilders.squadsense.models.UserModel;
import com.worthybitbuilders.squadsense.models.board_models.ProjectModel;
import com.worthybitbuilders.squadsense.utils.ActivityUtils;
import com.worthybitbuilders.squadsense.utils.Checking;
import com.worthybitbuilders.squadsense.utils.DialogUtils;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;
import com.worthybitbuilders.squadsense.utils.ToastUtils;
import com.worthybitbuilders.squadsense.viewmodels.FriendViewModel;
import com.worthybitbuilders.squadsense.viewmodels.ProjectActivityViewModel;
import com.worthybitbuilders.squadsense.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class MemberActivity extends AppCompatActivity {
    ActivityMemberBinding binding;
    MemberAdapter memberAdapter;
    List<UserModel> listMember = new ArrayList<>();
    List<UserModel> listFriend = new ArrayList<>();
    ProjectActivityViewModel projectActivityViewModel;
    UserViewModel userViewModel;
    FriendViewModel friendViewModel;
    FriendItemAdapter friendItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = ActivityMemberBinding.inflate(getLayoutInflater());
        projectActivityViewModel = new ViewModelProvider(this).get(ProjectActivityViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        friendItemAdapter = new FriendItemAdapter(listFriend);

        binding.rvMembers.setLayoutManager(new LinearLayoutManager(this));
        memberAdapter = new MemberAdapter(listMember);
        LoadData();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MemberActivity.this.onBackPressed();
            }
        });

        binding.btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.switchToActivity(MemberActivity.this, AddMemberActivity.class);
            }
        });


        setContentView(binding.getRoot());
    }

    private void LoadData()
    {
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(MemberActivity.this);
        loadingDialog.show();
        String projectId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.CURRENT_PROJECT_ID);
        projectActivityViewModel.getMember(projectId, new ProjectActivityViewModel.ApiCallMemberHandlers() {
            @Override
            public void onSuccess(List<UserModel> listMemberData) {
                listMember.clear();
                listMember.addAll(listMemberData);
                binding.rvMembers.setAdapter(memberAdapter);
                LoadListMemberView();
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showToastError(MemberActivity.this, "Can not load member of this project", Toast.LENGTH_LONG);
                loadingDialog.dismiss();
            }
        });
        memberAdapter.setOnClickListener(new MemberAdapter.OnActionCallback() {
            @Override
            public void OnClick(int position) {

            }
        });
    }

    private void LoadListMemberView()
    {
        if(listMember.size() > 0)
        {
            binding.rvMembers.setVisibility(View.VISIBLE);
            binding.screenNoMemberFound.setVisibility(View.GONE);
        }
        else
        {
            binding.rvMembers.setVisibility(View.GONE);
            binding.screenNoMemberFound.setVisibility(View.VISIBLE);
        }
    }
}