package com.example.monday_app_project.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.monday_app_project.R;

public class page_task_board extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_board);
        getSupportActionBar().hide();
    }
}