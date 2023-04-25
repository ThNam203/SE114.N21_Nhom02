package com.example.monday_app_project.Models;

import java.util.ArrayList;
import java.util.List;

public class Task {
    private List<String> message;
    private User person;
    private String date = "";
    public Task()
    {
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
