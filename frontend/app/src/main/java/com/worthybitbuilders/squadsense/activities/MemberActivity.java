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
import com.worthybitbuilders.squadsense.databinding.PopupAddMemberBinding;
import com.worthybitbuilders.squadsense.databinding.ProjectMoreOptionsBinding;
import com.worthybitbuilders.squadsense.models.UserModel;
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

    private void showAddMemberPopup() {
        final Dialog dialog = new Dialog(MemberActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PopupAddMemberBinding popupAddMemberBinding = PopupAddMemberBinding.inflate(getLayoutInflater());
        dialog.setContentView(popupAddMemberBinding.getRoot());
        popupAddMemberBinding.rvFriends.setLayoutManager(new LinearLayoutManager(MemberActivity.this));
        //Set activity of button in dialog here

        LoadListFriendInPopup(popupAddMemberBinding);

//        btnInvite.setOnClickListener(view -> {
//            String receiverEmail = inputEmail.getText().toString();
//
//            if(!Checking.IsValidEmail(receiverEmail))
//            {
//                ToastUtils.showToastError(MemberActivity.this, "Invalid email", Toast.LENGTH_SHORT);
//                return;
//            }
//
//            userViewModel.getUserByEmail(receiverEmail, new UserViewModel.UserCallback() {
//                @Override
//                public void onSuccess(UserModel receiver) {
//                    projectActivityViewModel.requestMemberToJoinProject(SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.CURRENT_PROJECT_ID), receiver.getId(), new ProjectActivityViewModel.ApiCallHandlers() {
//                        @Override
//                        public void onSuccess() {
//                            ToastUtils.showToastSuccess(MemberActivity.this, "Request was sent to " + receiverEmail + "!!", Toast.LENGTH_SHORT);
//                        }
//
//                        @Override
//                        public void onFailure(String message) {
//                            ToastUtils.showToastError(MemberActivity.this, message, Toast.LENGTH_SHORT);
//                        }
//                    });
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    ToastUtils.showToastError(MemberActivity.this, message, Toast.LENGTH_SHORT);
//                }
//            });
//        });

        popupAddMemberBinding.btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void LoadListFriendInPopup(PopupAddMemberBinding popupAddMemberBinding)
    {
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(MemberActivity.this);
        loadingDialog.show();
        String userId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USER_ID);
        friendViewModel.getFriendById(userId, new FriendViewModel.getFriendCallback() {
            @Override
            public void onSuccess(List<UserModel> friends) {
                listFriend.clear();
                listFriend.addAll(friends);

                popupAddMemberBinding.rvFriends.setAdapter(friendItemAdapter);
                LoadListFriendViewInPopup(popupAddMemberBinding);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showToastError(MemberActivity.this, message, Toast.LENGTH_SHORT);
                loadingDialog.dismiss();
            }
        });

        friendItemAdapter.setOnClickListener(new FriendItemAdapter.OnActionCallback() {
            @Override
            public void OnMoreOptionsClick(int position) {
                View anchor = popupAddMemberBinding.rvFriends.getLayoutManager().findViewByPosition(position);
                showAddMemberOptions(popupAddMemberBinding, anchor);
            }
        });
    }

    private void LoadListFriendViewInPopup(PopupAddMemberBinding popupAddMemberBinding)
    {
        if(listFriend.size() > 0)
        {
            popupAddMemberBinding.screenNoFriendFound.setVisibility(View.GONE);
            popupAddMemberBinding.rvFriends.setVisibility(View.VISIBLE);
        }
        else
        {
            popupAddMemberBinding.screenNoFriendFound.setVisibility(View.VISIBLE);
            popupAddMemberBinding.rvFriends.setVisibility(View.GONE);
        }
    }

    private void showAddMemberOptions(PopupAddMemberBinding popupAddMemberBinding, View anchor)
    {
        AddMemberMoreOptionsBinding binding = AddMemberMoreOptionsBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(binding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(50);

        binding.btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = popupAddMemberBinding.rvFriends.getChildAdapterPosition(view);
                String receiverEmail = listFriend.get(position).getEmail();

                userViewModel.getUserByEmail(receiverEmail, new UserViewModel.UserCallback() {
                @Override
                public void onSuccess(UserModel receiver) {
                    projectActivityViewModel.requestMemberToJoinProject(SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.CURRENT_PROJECT_ID), receiver.getId(), new ProjectActivityViewModel.ApiCallHandlers() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showToastSuccess(MemberActivity.this, "Request was sent!", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(String message) {
                            ToastUtils.showToastError(MemberActivity.this, message, Toast.LENGTH_SHORT);
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    ToastUtils.showToastError(MemberActivity.this, message, Toast.LENGTH_SHORT);
                }
            });
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        int xOffset = anchor.getWidth(); // Offset from the right edge of the anchor view
        popupWindow.showAsDropDown(anchor, xOffset, 0);
    }
}