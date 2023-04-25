package com.example.monday_app_project.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.monday_app_project.R;

public class page_task_board extends AppCompatActivity {

    ImageButton btnBack = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_board);
        getSupportActionBar().hide();

        btnBack = (ImageButton) findViewById(R.id.btn_back);
        TableLayout taskboard = (TableLayout) findViewById(R.id.taskboard);
        TableRow task1 = (TableRow) getLayoutInflater().inflate(R.layout.listview_item_custom_row, null);
        TableRow task2 = (TableRow) getLayoutInflater().inflate(R.layout.listview_item_custom_row, null);
        taskboard.addView(task1);
        taskboard.addView(task2);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_task_board.super.onBackPressed();
            }
        });

    }
}