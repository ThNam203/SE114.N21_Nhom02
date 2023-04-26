package com.example.monday_app_project.Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.monday_app_project.R;

import java.util.ArrayList;
import java.util.List;

public class page_task_board extends AppCompatActivity {

    ImageButton btnBack = null;
    List<TableLayout> taskBoards = new ArrayList<TableLayout>();

    HorizontalScrollView horizontalScrollView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_task_board);
        getSupportActionBar().hide();

        //Init variables here
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);


        //Set up taskboard
        AddNewTaskBoard(taskBoards,3);

        //set OnClick of buttons here
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_task_board.super.onBackPressed();
            }
        });

    }


    //define function here
    private void AddNewTask(TableLayout taskBoard, int numOfRow)
    {
        for(int i = 0; i < numOfRow; i++)
        {
            TableRow newTaskRow = (TableRow) getLayoutInflater().inflate(R.layout.taskboard_row, null);

            TextView taskName = (TextView) newTaskRow.findViewById(R.id.task_name);
            taskName.setText("Item " + String.valueOf(i + 1));

            taskBoard.addView(newTaskRow);
        }
    }

    private void AddNewTaskBoard(List<TableLayout> taskBoards, int numOfRowInNewTaskBoard)
    {
        TableLayout newTaskBoard = (TableLayout) getLayoutInflater().inflate(R.layout.taskboard_header, null);
        AddNewTask(newTaskBoard, 3);
        taskBoards.add(newTaskBoard);
        horizontalScrollView.addView(newTaskBoard);
    }
}