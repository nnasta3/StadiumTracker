package com.example.stadiumtracker.data;

import java.io.Serializable;

public class User implements Serializable {
    int userID;
    String name;

    public User(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
