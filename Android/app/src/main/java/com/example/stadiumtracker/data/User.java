package com.example.stadiumtracker.data;


public class User implements java.io.Serializable {
    private int userID;
    private String name;

    /* John Strauser
        simple constructor for the user class. We do not store a password in this class for security purposes.
     */
    public User(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    /* John Strauser
        Rest of the class is auto-generated getters and setters
     */

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
