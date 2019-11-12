package com.example.stadiumtracker.helpers;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;

public class StadiumListHelper {
    private Stadium stadium;
    private User user;
    private int visits;

    public StadiumListHelper() {
        stadium = null;
        user = null;
        visits = 0;
    }

    public StadiumListHelper(Stadium stadium, User user, int visits) {
        this.stadium = stadium;
        this.user = user;
        this.visits = visits;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
