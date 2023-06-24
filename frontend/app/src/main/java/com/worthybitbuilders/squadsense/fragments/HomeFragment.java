package com.worthybitbuilders.squadsense.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.activities.AddBoardActivity;
import com.worthybitbuilders.squadsense.activities.ProjectActivity;
import com.worthybitbuilders.squadsense.activities.SearchActivity;
import com.worthybitbuilders.squadsense.adapters.ProjectAdapter;
import com.worthybitbuilders.squadsense.databinding.FragmentHomeBinding;
import com.worthybitbuilders.squadsense.databinding.MemberMoreOptionsBinding;
import com.worthybitbuilders.squadsense.databinding.PopupBtnAddBinding;
import com.worthybitbuilders.squadsense.databinding.PopupFavoriteProjectBinding;
import com.worthybitbuilders.squadsense.models.UserModel;
import com.worthybitbuilders.squadsense.utils.ActivityUtils;
import com.worthybitbuilders.squadsense.utils.DialogUtils;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;
import com.worthybitbuilders.squadsense.utils.ToastUtils;
import com.worthybitbuilders.squadsense.viewmodels.FriendViewModel;
import com.worthybitbuilders.squadsense.viewmodels.MainActivityViewModel;
import com.worthybitbuilders.squadsense.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ProjectAdapter projectAdapter;
    private MainActivityViewModel viewModel;

    private FriendViewModel friendViewModel;
    private UserViewModel userViewModel;
    private List<AppCompatButton> listOption = new ArrayList<>();
    private int selectedOptionIndex = -1;

    @SuppressLint({"ClickableViewAccessibility", "NotifyDataSetChanged"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        friendViewModel = new ViewModelProvider(getActivity()).get(FriendViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        LoadData();

        binding.btnOptionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnOptionView_showPopup();
            }
        });
        binding.btnAddperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_addperson_showPopup();

            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAdd_showPopup(view);
            }
        });
        binding.labelSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;
                if (action == MotionEvent.ACTION_UP) {
                    labelSearch_showActivity();
                }
                return true;
            }
        });
        return binding.getRoot();
    }

    //define function here

    private void LoadData()
    {
        // THERE IS ONLY "ONFAILURE" method
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(getContext());
        loadingDialog.show();
        viewModel.getAllProjects(new MainActivityViewModel.GetProjectsFromRemoteHandlers() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
            }
            @Override
            public void onFailure(String message) {
                ToastUtils.showToastError(getContext(), message, Toast.LENGTH_LONG);
                loadingDialog.dismiss();
            }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnAdd_showPopup(View anchor) {
        PopupBtnAddBinding popupBtnAddBinding = PopupBtnAddBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(popupBtnAddBinding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(R.style.PopupAnimationRight);
        popupWindow.setElevation(50);

        popupBtnAddBinding.btnAddItem.setOnClickListener(view -> btn_add_item_showPopup());
        popupBtnAddBinding.btnAddBoard.setOnClickListener(view -> btn_add_board_showPopup());

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        int xOffset = - 3 * anchor.getWidth();
        int yOffset = - 3 * anchor.getHeight();
        popupWindow.showAsDropDown(anchor, xOffset, yOffset);
    }

    private void btn_addperson_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_invite_by_email);

        //Set activity of button in dialog here
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        AppCompatButton btnInvite = (AppCompatButton) dialog.findViewById(R.id.btn_invite);
        EditText inputEmail = (EditText) dialog.findViewById(R.id.input_email);


        btnInvite.setOnClickListener(view -> {
            String receiverEmail = inputEmail.getText().toString();

            if(!friendViewModel.IsValidEmail(receiverEmail))
            {
                ToastUtils.showToastError(getContext(), "Invalid email", Toast.LENGTH_SHORT);
                return;
            }

            userViewModel.getUserByEmail(receiverEmail, new UserViewModel.UserCallback() {
                @Override
                public void onSuccess(UserModel user) {
                    friendViewModel.createRequest(SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USER_ID), user.getId(), new FriendViewModel.FriendRequestCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showToastSuccess(getContext(), "Request was sent to " + receiverEmail + "!!", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(String message) {
                            ToastUtils.showToastError(getContext(), message, Toast.LENGTH_SHORT);
                        }
                    });
                }

                @Override
                public void onFailure(String message) {
                    ToastUtils.showToastError(getContext(), message, Toast.LENGTH_SHORT);
                }
            });
        });

        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void btn_add_item_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_new_item);

        //Set activity of button in dialog here
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void btn_add_board_showPopup() {
        ActivityUtils.switchToActivity(getContext(), AddBoardActivity.class);
    }

    private void btnOptionView_showPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PopupFavoriteProjectBinding popupFavoriteProjectBinding = PopupFavoriteProjectBinding.inflate(getLayoutInflater());
        dialog.setContentView(popupFavoriteProjectBinding.getRoot());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        setupListOption(popupFavoriteProjectBinding);

        //      Set activity of button in dialog here
        popupFavoriteProjectBinding.btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        popupFavoriteProjectBinding.optionRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedOption(popupFavoriteProjectBinding.optionRecent);
            }
        });

        popupFavoriteProjectBinding.optionAllPorject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedOption(popupFavoriteProjectBinding.optionAllPorject);
            }
        });
    }

    private void setupListOption(PopupFavoriteProjectBinding popupFavoriteProjectBinding)
    {
        listOption.clear();
        listOption.add(popupFavoriteProjectBinding.optionRecent);
        listOption.add(popupFavoriteProjectBinding.optionAllPorject);

        if(selectedOptionIndex == -1)
        {
            setSelectedOption(popupFavoriteProjectBinding.optionAllPorject);
            selectedOptionIndex = listOption.indexOf(popupFavoriteProjectBinding.optionAllPorject);
        }
        else
        {
            setSelectedOption(listOption.get(selectedOptionIndex));
        }
    }


    private void setSelectedOption(AppCompatButton option)
    {
        int chosenOptionColor = ContextCompat.getColor(getContext(), R.color.chosen_color);
        int primaryOptionColor = ContextCompat.getColor(getContext(), R.color.primary_word_color);

        listOption.forEach(item -> {
            setDrawableRight(item, null);
            changeColorButton(item, primaryOptionColor);
        });

        selectedOptionIndex = listOption.indexOf(option);
        Drawable drawableRight = getResources().getDrawable(R.drawable.ic_tick, null);
        setDrawableRight(option, drawableRight);
        changeColorButton(option, chosenOptionColor);

        binding.titleOptionView.setText(option.getText().toString());
    }

    private void changeColorButton(AppCompatButton btn, int color)
    {
        btn.setTextColor(color);
        btn.setCompoundDrawableTintList(ColorStateList.valueOf(color));
    }

    private void setDrawableRight(AppCompatButton button, Drawable drawableRight) {
        button.post(new Runnable() {
            @Override
            public void run() {
                Drawable[] drawables = button.getCompoundDrawables();
                button.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawableRight, drawables[3]);
            }
        });
    }

    private void labelSearch_showActivity() {
        ActivityUtils.switchToActivity(getContext(), SearchActivity.class);
    }
}