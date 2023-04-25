package com.example.monday_app_project.Models;

import java.util.List;

public class Board {
    private String title = "";
    private List<Task> tasks;
    public Board()
    {

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
