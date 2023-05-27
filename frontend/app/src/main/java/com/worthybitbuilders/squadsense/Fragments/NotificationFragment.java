package com.worthybitbuilders.squadsense.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.worthybitbuilders.squadsense.models.Notification;
import com.worthybitbuilders.squadsense.models.UserModel;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.viewmodels.FriendViewModel;
import com.worthybitbuilders.squadsense.viewmodels.NotificationViewModel;
import com.worthybitbuilders.squadsense.viewmodels.UserViewModel;
import com.worthybitbuilders.squadsense.adapters.NotificationAdapter;
import com.worthybitbuilders.squadsense.databinding.FragmentNotificationBinding;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    private Button selectedButton = null;
    private List<Button> buttonList = new ArrayList<>();
    private FragmentNotificationBinding binding;
    private NotificationViewModel notificationViewModel;
    private FriendViewModel friendViewModel;

    private NotificationAdapter notificationAdapter;

    private UserViewModel userViewModel;
    private List<Notification> listNotification = new ArrayList<>();

    private Notification selectedNotification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(getLayoutInflater());
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupBtnList();

        notificationViewModel.getNotification(SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USERID), new NotificationViewModel.getNotificationCallback() {
            @Override
            public void onSuccess(List<Notification> notificationsData) {
                for (Notification item : notificationsData) {
                    Notification notification = new Notification();
                    notification.setId(item.getId());
                    notification.setSenderId(item.getSenderId());
                    notification.setReceiverId(item.getReceiverId());
                    notification.setNotificationType(item.getNotificationType());
                    notification.setTitle(item.getTitle());
                    notification.setContent(item.getContent());
                    notification.setIsRead(item.getIsRead());
                    notification.setLink(item.getLink());
                    listNotification.add(notification);
                }
                ReloadNotificationView();
            }

            @Override
            public void onFailure(String message) {
                Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP, 0, 0);
                t.show();
            }
        });

        binding.recyclerviewNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(getContext(), listNotification);
        binding.recyclerviewNotification.setAdapter(notificationAdapter);

        notificationAdapter.setOnReplyListener(new NotificationAdapter.OnActionCallback() {
            @Override
            public void OnAccept(int position) {
                String response = "Accept";
                Reply(response, position);

                listNotification.remove(position);
                binding.recyclerviewNotification.setAdapter(notificationAdapter);
                ReloadNotificationView();
            }

            @Override
            public void OnDeny(int position) {
                String response = "Deny";
                Reply(response, position);

                listNotification.remove(position);
                binding.recyclerviewNotification.setAdapter(notificationAdapter);
                ReloadNotificationView();
            }

            @Override
            public void OnShowingOption(int position) {
                View notification = binding.recyclerviewNotification.getChildAt(position);
                registerForContextMenu(notification);
                selectedNotification = listNotification.get(position);
            }
        });

        //set onclick buttons here
        binding.btnInviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_invite_showDialog();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.notification_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_mark_as_read:
                selectedNotification.setIsRead(true);
                return true;
            case R.id.option_delete:
            {
                notificationViewModel.deleteNotification(selectedNotification.getId(), new NotificationViewModel.deleteNotificationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast t = Toast.makeText(getContext(), "notification deleted", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.TOP, 0, 0);
                        t.show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.TOP, 0, 0);
                        t.show();
                    }
                });
                listNotification.remove(selectedNotification);
                binding.recyclerviewNotification.setAdapter(notificationAdapter);
                ReloadNotificationView();
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void ReloadNotificationView()
    {
        if(listNotification.size() > 0)
        {
            binding.defaultviewNotification.setVisibility(View.GONE);
            binding.recyclerviewNotification.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.defaultviewNotification.setVisibility(View.VISIBLE);
            binding.recyclerviewNotification.setVisibility(View.GONE);
        }
    }

    private void Reply(String response, int position){
        String replierId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USERID);
        String requestSender = listNotification.get(position).getSenderId();
        friendViewModel.reply(replierId, requestSender, response, new FriendViewModel.FriendRequestCallback() {
            @Override
            public void onSuccess() {
                Toast t = Toast.makeText(getContext(), "reply was sent!!", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP,0,0);
                t.show();
            }

            @Override
            public void onFailure(String message) {
                Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.TOP,0,0);
                t.show();
            }
        });
    }

    //define function here
    private void btn_invite_showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_btn_invite_by_email);

        //Set activity of button in dialog here
        EditText inputEmail = (EditText) dialog.findViewById(R.id.input_email);
        inputEmail.requestFocus();
        AppCompatButton btnInvite = (AppCompatButton) dialog.findViewById(R.id.btn_invite);

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverEmail = inputEmail.getText().toString();

                if(!friendViewModel.IsValidEmail(receiverEmail))
                {
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                userViewModel.getUserByEmail(receiverEmail, new UserViewModel.UserCallback() {
                    @Override
                    public void onSuccess(UserModel user) {
                        friendViewModel.createRequest(SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USERID), user.getId(), new FriendViewModel.FriendRequestCallback() {
                            @Override
                            public void onSuccess() {
                                Toast t = Toast.makeText(getContext(), "request was sent to " + receiverEmail + "!!", Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.TOP, 0, 0);
                                t.show();
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                                t.setGravity(Gravity.TOP, 0, 0);
                                t.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.TOP, 0, 0);
                        t.show();
                    }
                });
            }
        });
        //

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        ImageButton btnClosePopupBtnInvite = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        btnClosePopupBtnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void setupBtnList()
    {
        //Add button to list buttons in horizontal scrollview
        buttonList.add(binding.btnAllNotification);
        buttonList.add(binding.btnUnreadNotification);
        buttonList.add(binding.btnMentionedNotification);
        buttonList.add(binding.btnAssignToMeNotification);
        buttonList.add(binding.btnTodayNotification);

        //Algorithm to set click buttons in horizontal scrollview to choose just one button each time
        selectedButton = binding.btnAllNotification;
        binding.btnAllNotification.setSelected(true);

        for(Button btn : buttonList)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedButton.setSelected(false);
                    btn.setSelected(true);
                    selectedButton = btn;
                }
            });
        }
    }
}