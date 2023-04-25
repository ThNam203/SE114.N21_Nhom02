package com.example.monday_app_project.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.monday_app_project.R;

public class page_add_board extends AppCompatActivity {

    ImageButton btnClose = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_add_board);
        getSupportActionBar().hide();

        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_add_board.super.onBackPressed();
            }
        });
    }
}