package com.example.monday_app_project.Models;

import android.content.Context;
import android.widget.TableLayout;

import java.util.List;

public class TaskBoard extends TableLayout {
    private String title = "";
    private List<Task> tasks;
    public TaskBoard(Context context)
    {
        super(context);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
