package com.example.monday_app_project.Models;

import android.content.Context;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

public class Task extends TableRow {
    private List<String> message;
    private User person;
    private String date = "";
    public Task(Context context)
    {
        super(context);
        message = new ArrayList<String>();
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public User getPerson() {
        return person;
    }

    public void setPerson(User person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
