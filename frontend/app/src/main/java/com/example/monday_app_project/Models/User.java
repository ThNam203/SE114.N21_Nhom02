package com.example.monday_app_project.Models;

import com.example.monday_app_project.Util.Checking;

public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean IsRightEmail(String inputEmail)
    {
        return Checking.IsValidEmail(email) && inputEmail == this.email;
    }

    public boolean IsRightPassword(String InputPassword)
    {
        return InputPassword == this.password;
    }


}
