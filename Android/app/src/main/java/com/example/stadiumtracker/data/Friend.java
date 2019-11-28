package com.example.stadiumtracker.data;



import java.sql.Date;

public class Friend implements java.io.Serializable{
    private String friendUsername;
    private Date startOfFriendship;

    public Friend(String friendUsername, Date startOfFriendship) {
        this.friendUsername = friendUsername;
        this.startOfFriendship = startOfFriendship;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public Date getDateTime() {
        return startOfFriendship;
    }

}
