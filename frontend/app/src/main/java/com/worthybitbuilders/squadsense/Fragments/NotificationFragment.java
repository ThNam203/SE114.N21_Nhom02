package com.worthybitbuilders.squadsense.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.worthybitbuilders.squadsense.Models.UserModel;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.ViewModels.FriendViewModel;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    Button btnInviteMember = null;
    Button btnAllNotification = null;
    Button btnUnreadNotification = null;
    Button btnMentionedNotification = null;
    Button btnAssignedNotification = null;
    Button btnTodayNotification = null;

    Button selectedButton = null;
    List<Button> buttonList = new ArrayList<Button>();

    FriendViewModel friendViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        //Init variables here
        btnInviteMember = (Button) v.findViewById(R.id.btn_invite_member);
        btnAllNotification = (Button) v.findViewById(R.id.btn_all_notification);
        btnUnreadNotification =  (Button) v.findViewById(R.id.btn_unread_notification);
        btnMentionedNotification =  (Button) v.findViewById(R.id.btn_mentioned_notification);
        btnAssignedNotification =  (Button) v.findViewById(R.id.btn_assign_to_me_notification);
        btnTodayNotification =  (Button) v.findViewById(R.id.btn_today_notification);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        //Add button to list buttons in horizontal scrollview
        buttonList.add(btnAllNotification);
        buttonList.add(btnUnreadNotification);
        buttonList.add(btnMentionedNotification);
        buttonList.add(btnAssignedNotification);
        buttonList.add(btnTodayNotification);

        //Algorithm to set click buttons in horizontal scrollview to choose just one button each time
        selectedButton = btnAllNotification;
        btnAllNotification.setSelected(true);

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

        //set onclick buttons here
        btnInviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_invite_showDialog();
            }
        });


        return v;
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

//        btnInvite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String receiverEmail = inputEmail.getText().toString();
//                friendViewModel.checkUserByEmail(receiverEmail, new FriendViewModel.FriendRequestCallback() {
//                    @Override
//                    public void onSuccess(UserModel user) {
//                        Toast t = Toast.makeText(getContext(), user.getId().toString(), Toast.LENGTH_SHORT);
//                        t.setGravity(Gravity.TOP, 0, 0);
//                        t.show();
//                    }
//
//                    @Override
//                    public void onFailure(String message) {
//                        Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
//                        t.setGravity(Gravity.TOP, 0, 0);
//                        t.show();
//                    }
//                });
//            }
//        });

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
}