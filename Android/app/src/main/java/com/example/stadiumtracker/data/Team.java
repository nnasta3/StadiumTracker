package com.example.stadiumtracker.data;

public class Team implements java.io.Serializable{
    private int teamID;
    private String city;
    private String nickname;
    private String abbrev;
    private Stadium stadium;
    private String league;

    public Team(int teamID, String city, String nickname, String abbrev, Stadium stadium, String league) {
        this.teamID = teamID;
        this.city = city;
        this.nickname = nickname;
        this.abbrev = abbrev;
        this.stadium = stadium;
        this.league = league;
    }

    public Team(boolean home){
        teamID = -1;
        if(home){
            city = "Home";
            abbrev = "HT";
        }else{
            city = "Road";
            abbrev = "RT";
        }
        nickname = "Team";
        stadium = null;
        league = "";
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String toString(){
        return city + " " + nickname;
    }
}
