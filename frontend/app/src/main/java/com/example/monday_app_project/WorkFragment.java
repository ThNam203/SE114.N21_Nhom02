package com.example.monday_app_project;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WorkFragment extends Fragment {

    Button btnHideDoneItem = null;
    Boolean IsbtnHideDoneItemChecked = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_work, container, false);

        btnHideDoneItem = (Button) v.findViewById(R.id.btn_hide_done_item);
        btnHideDoneItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(IsbtnHideDoneItemChecked == false)
                {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_checkbox_checked);
                    drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.chosen_color), PorterDuff.Mode.SRC_IN);
                    btnHideDoneItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

                    IsbtnHideDoneItemChecked = true;
                }
                else
                {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_checkbox_unchecked);
                    drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.primary_icon_color), PorterDuff.Mode.SRC_IN);
                    btnHideDoneItem.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

                    IsbtnHideDoneItemChecked = false;
                }

            }
        });


        return v;
    }
}