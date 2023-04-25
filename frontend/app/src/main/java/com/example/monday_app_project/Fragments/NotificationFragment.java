package com.example.monday_app_project.Fragments;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.example.monday_app_project.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    Button btnInvite = null;
    Button btnAllNotification = null;
    Button btnUnreadNotification = null;
    Button btnMentionedNotification = null;
    Button btnAssignedNotification = null;
    Button btnTodayNotification = null;

    Button selectedButton = null;
    List<Button> buttonList = new ArrayList<Button>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        btnInvite = (Button) v.findViewById(R.id.btn_invite);
        btnAllNotification = (Button) v.findViewById(R.id.btn_all_notification);
        btnUnreadNotification =  (Button) v.findViewById(R.id.btn_unread_notification);
        btnMentionedNotification =  (Button) v.findViewById(R.id.btn_mentioned_notification);
        btnAssignedNotification =  (Button) v.findViewById(R.id.btn_assign_to_me_notification);
        btnTodayNotification =  (Button) v.findViewById(R.id.btn_today_notification);

        buttonList.add(btnAllNotification);
        buttonList.add(btnUnreadNotification);
        buttonList.add(btnMentionedNotification);
        buttonList.add(btnAssignedNotification);
        buttonList.add(btnTodayNotification);

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

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_invite_showDialog();
            }
        });


        return v;
    }

    private void btn_invite_showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_btn_invite_by_email);

        //Set activity of button in dialog here


        //


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        EditText inputEmail = (EditText) dialog.findViewById(R.id.input_email);
        inputEmail.requestFocus();

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