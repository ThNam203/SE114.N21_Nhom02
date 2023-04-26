package com.example.monday_app_project.Pages;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
            TextView taskStatus = (TextView) newTaskRow.findViewById(R.id.task_status);
            taskName.setText("Item " + String.valueOf(i + 1));
            taskStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTaskStatusPopup(taskStatus);

                }
            });

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

    private void showTaskStatusPopup(TextView taskStatus)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_status);

        //init variables here
        ImageButton btnClosePopup = (ImageButton) dialog.findViewById(R.id.btn_close_popup);
        TextView statusWorking = (TextView) dialog.findViewById(R.id.status_working);
        TextView statusStuck = (TextView) dialog.findViewById(R.id.status_stuck);
        TextView statusDone = (TextView) dialog.findViewById(R.id.status_done);

        //set onclick buttons here
        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        statusWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskStatus.setText("Working on it");
                taskStatus.setBackgroundColor(getResources().getColor(R.color.orange));
                dialog.dismiss();
            }
        });
        statusStuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskStatus.setText("Stuck");
                taskStatus.setBackgroundColor(getResources().getColor(R.color.red));
                dialog.dismiss();
            }
        });
        statusDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskStatus.setText("Done");
                taskStatus.setBackgroundColor(getResources().getColor(R.color.green));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();


    }

}