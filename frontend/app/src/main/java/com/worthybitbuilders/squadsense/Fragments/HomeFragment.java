package com.worthybitbuilders.squadsense.fragments;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.activities.ProjectActivity;
import com.worthybitbuilders.squadsense.activities.page_add_board;
import com.worthybitbuilders.squadsense.activities.page_search;
import com.worthybitbuilders.squadsense.adapters.ProjectAdapter;
import com.worthybitbuilders.squadsense.databinding.FragmentHomeBinding;
import com.worthybitbuilders.squadsense.utils.SwitchActivity;
import com.worthybitbuilders.squadsense.viewmodels.MainActivityViewModel;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProjectAdapter projectAdapter;
    private MainActivityViewModel viewModel;

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        // THERE IS ONLY "ONFAILURE" method
        viewModel.getAllProjects(message -> {
            Toast.makeText(getContext(), "Unable to get your projects, please try again", Toast.LENGTH_LONG).show();
        });

        viewModel.getProjectsLiveData().observe(getViewLifecycleOwner(), minimizedProjectModels -> {
            if (minimizedProjectModels == null || minimizedProjectModels.size() == 0)
                binding.emptyProjectsContainer.setVisibility(View.VISIBLE);
            else binding.emptyProjectsContainer.setVisibility(View.GONE);
            projectAdapter.setData(minimizedProjectModels);
        });

        projectAdapter = new ProjectAdapter(null, _id -> {
            Intent intent = new Intent(getContext(), ProjectActivity.class);
            intent.putExtra("whatToDo", "fetch");
            intent.putExtra("projectId", _id);
            startActivity(intent);
        });
        binding.rvProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvProjects.setHasFixedSize(true);
        binding.rvProjects.setAdapter(projectAdapter);

        //set onclick buttons here
        binding.btnMyfavorites.setOnClickListener(view -> btn_myfavorities_showPopup());
        binding.btnAddperson.setOnClickListener(view -> btn_addperson_showPopup());
        binding.btnAdd.setOnClickListener(view -> btnAdd_showPopup());
        binding.inputSearchLabel.setOnTouchListener((view, motionEvent) -> {
            int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;

            if (action == MotionEvent.ACTION_UP) {
                inputSearch_showActivity();
            }
            return true;
        });



        return binding.getRoot();
    }


    //define function here
    @SuppressLint("ClickableViewAccessibility")
    private void btnAdd_showPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_btn_add, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        PopupWindow popupBtnAdd = new PopupWindow(popupView,width,height, true);
        popupBtnAdd.setAnimationStyle(R.style.PopupAnimationRight);
        binding.getRoot().post(() -> popupBtnAdd.showAtLocation(binding.getRoot(), Gravity.RIGHT, 0, 550));

        popupView.setOnTouchListener((view, motionEvent) -> {
            popupBtnAdd.dismiss();
            return true;
        });
        LinearLayout btnAddItem = popupView.findViewById(R.id.btn_add_item);
        btnAddItem.setOnClickListener(view -> btn_add_item_showPopup());

        LinearLayout btnAddBoard = popupView.findViewById(R.id.btn_add_board);
        btnAddBoard.setOnClickListener(view -> btn_add_board_showPopup());
    }

    private void btn_addperson_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_btn_invite_by_email);

        //Set activity of button in dialog here


        //


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void btn_add_item_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_new_item);

        //Set activity of button in dialog here


        //


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void btn_add_board_showPopup() {
        SwitchActivity.switchToActivity(getContext(), page_add_board.class);
    }

    private void btn_myfavorities_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_btn_myfavorite);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();



        //      Set activity of button in dialog here
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        LinearLayout optionRecent = (LinearLayout) dialog.findViewById(R.id.option_recent);
        LinearLayout optionMyfavorite = (LinearLayout) dialog.findViewById(R.id.option_myfavorite);

        ImageView iconRecent = (ImageView) dialog.findViewById(R.id.option_recent_icon);
        TextView titleRecent = (TextView) dialog.findViewById(R.id.option_recent_title);
        ImageView tickRecent = (ImageView) dialog.findViewById(R.id.option_recent_tick);
        ImageView iconMyfavorite = (ImageView) dialog.findViewById(R.id.option_myfavorite_icon);
        TextView titleMyfavorite = (TextView) dialog.findViewById(R.id.option_myfavorite_title);
        ImageView tickMyfavorite = (ImageView) dialog.findViewById(R.id.option_myfavorite_tick);

        int chosenColor = getResources().getColor(R.color.chosen_color);
        int defaultColor = getResources().getColor(R.color.primary_icon_color);

        updateButtonState(iconRecent, titleRecent, tickRecent, chosenColor, true);
        optionRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonState(iconRecent, titleRecent, tickRecent, chosenColor, true);
                updateButtonState(iconMyfavorite, titleMyfavorite, tickMyfavorite, defaultColor, false);
            }
        });

        optionMyfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonState(iconRecent, titleRecent, tickRecent, defaultColor, false);
                updateButtonState(iconMyfavorite, titleMyfavorite, tickMyfavorite, chosenColor, true);
            }
        });

        //
    }



    private void updateButtonState(ImageView icon, TextView title, ImageView tick, int color, boolean tickState)
    {
        icon.setColorFilter(color);
        title.setTextColor(color);
        if(tickState)  tick.setVisibility(View.VISIBLE);
        else tick.setVisibility(View.INVISIBLE);
    }
    private void inputSearch_showActivity() {
        SwitchActivity.switchToActivity(getContext(), page_search.class);
    }
}