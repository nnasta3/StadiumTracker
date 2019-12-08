package com.example.stadiumtracker.data;

public class Team implements java.io.Serializable{
    private int teamID;
    private String city;
    private String nickname;
    private String abbrev;
    private Stadium stadium;
    private String league;

    /* John Strauser
        Generic constructor for a team instance
     */
    public Team(int teamID, String city, String nickname, String abbrev, Stadium stadium, String league) {
        this.teamID = teamID;
        this.city = city;
        this.nickname = nickname;
        this.abbrev = abbrev;
        this.stadium = stadium;
        this.league = league;
    }
    /* John Strauser
        Constructor for a generic home or road team. Only used to create the 'prompt' entries in RecordActivity's team dropdown menus.
        Takes a boolean to determine if it is a home or road team.
     */
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
    /* John Strauser
        Auto-generated getters and setters
     */
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
    /* John Strauser
        to string used in many places.
        Example output would be "New York Yankees"
     */
    public String toString(){
        return city + " " + nickname;
    }
}
