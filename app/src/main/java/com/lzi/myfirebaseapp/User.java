package com.lzi.myfirebaseapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;

    public User(){}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
