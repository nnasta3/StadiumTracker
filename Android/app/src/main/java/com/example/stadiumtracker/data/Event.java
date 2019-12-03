package com.example.stadiumtracker.data;

import java.util.Calendar;

public class Event implements java.io.Serializable{
    int eventID;
    int stadiumID;
    Calendar date;
    String homeTeam;
    String roadTeam;
    int homeScore;
    int roadScore;
    String league;

    public Event(){

    }

    public Event(int eventID, int stadiumID, Calendar date, String homeTeam, String roadTeam, int homeScore, int roadScore, String league) {
        this.eventID = eventID;
        this.stadiumID = stadiumID;
        this.date = date;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.homeScore = homeScore;
        this.roadScore = roadScore;
        this.league = league;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getStadiumID() {
        return stadiumID;
    }

    public void setStadiumID(int stadiumID) {
        this.stadiumID = stadiumID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(String roadTeam) {
        this.roadTeam = roadTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getRoadScore() {
        return roadScore;
    }

    public void setRoadScore(int roadScore) {
        this.roadScore = roadScore;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getDateFullString(){
        String [] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        return months[date.get(Calendar.MONTH)]+" "+date.get(Calendar.DAY_OF_MONTH)+", "+date.get(Calendar.YEAR);
    }
}
