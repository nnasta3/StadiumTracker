package com.example.stadiumtracker.data;

import java.util.Calendar;

public class Event implements java.io.Serializable{
    private int eventID;
    private Stadium stadium;
    private Calendar date;
    private Team homeTeam;
    private Team roadTeam;
    private int homeScore;
    private int roadScore;
    private String league;

    /* John Strauser
        Generic constructor for the Event class
    */
    public Event(int eventID, Stadium stadium, Calendar date, Team homeTeam, Team roadTeam, int homeScore, int roadScore, String league) {
        this.eventID = eventID;
        this.stadium = stadium;
        this.date = date;
        this.homeTeam = homeTeam;
        this.roadTeam = roadTeam;
        this.homeScore = homeScore;
        this.roadScore = roadScore;
        this.league = league;
    }
    /* John Strauser
        Auto-generated getters and setters
     */
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadiumID(Stadium stadium) {
        this.stadium = stadium;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getRoadTeam() {
        return roadTeam;
    }

    public void setRoadTeam(Team roadTeam) {
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

    /* John Strauser
        Provides the date in the format "Month Day, Year"
        Used primarily in listviews found in VisitViewActivity and StadiumViewActivity
     */
    public String getDateFullString(){
        String [] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        return months[date.get(Calendar.MONTH)]+" "+date.get(Calendar.DAY_OF_MONTH)+", "+date.get(Calendar.YEAR);
    }

    /* John Strauser
        Provides a string to be used as a file name for saving the virtual ticket stub that corresponds to this event.
        In the test environment this is not guaranteed to be unique.
     */
    public String fileString(){
        return roadTeam.getNickname()+"@"+homeTeam.getNickname()+"-"+(date.get(Calendar.MONTH)+1)+"-"+date.get(Calendar.DAY_OF_MONTH)+"-"+date.get(Calendar.YEAR);
    }
}
