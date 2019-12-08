package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.Team;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class allEventsForUser extends AsyncTask<Integer, Void, List<Event>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;
    Map<Integer, Stadium> stadiumMap;
    Map<Integer, Team> teamMap;
    private Context context;
    /* John Strauser
        constructor for the allEventsForUser query class
        context is used in setStrings
     */
    public allEventsForUser(Context context){
        super();
        this.context = context;
        setStrings();
    }
    /* John Strauser
        SetStrings should be present in every query function
        Provides the class with the information needed to connect to the database
     */
    private void setStrings(){
        ip = context.getResources().getString(R.string.ip);
        port = context.getResources().getString(R.string.port);
        dbName = context.getResources().getString(R.string.db_name);
        user = context.getResources().getString(R.string.masterUser);
        pass = context.getResources().getString(R.string.masterPass);
    }
    /* John Strauser
        onPreExecute runs after allEventsForUser.execute() is called and before doInBackground() runs
        calls queries for allStadiums and allTeams
        creates hash maps for both stadiums and teams
        stadium map has keys of stadiumID and values of stadium instances
        team map has keys of teamID and values of team instances
        Used to reduce the number of queries used
     */
    @Override
    protected void onPreExecute(){
        try{
            List<Stadium> stadiums = new allStadiums(context).execute().get();
            List<Team> teams = new allTeams(context).execute().get();
            stadiumMap = new HashMap<>();
            teamMap = new HashMap<>();
            for (Stadium stadium : stadiums){
                stadiumMap.put(stadium.getStadiumID(),stadium);
            }
            for (Team team : teams){
                teamMap.put(team.getTeamID(),team);
            }
        }catch (Exception e){
            Log.e("aEFU pre",e.toString());
        }
    }
    /* John Strauser
        takes a userID as input
        returns a list of events that the userID has visited
     */
    @Override
    protected List<Event> doInBackground(Integer... integers) {
        //integers[0] = userID
        Log.e("aEFU","starting doinb");
        List<Event> events = new ArrayList<>();
        try{
            Log.e("aEFU","done mapping. main query now");
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Event] WHERE EventID IN (SELECT EventID FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID=?) ORDER BY Date DESC");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int eventID = rs.getInt(1);
                Stadium stadium = stadiumMap.get(rs.getInt(2));
                Date date = rs.getDate(3);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                Team homeTeam = teamMap.get(rs.getInt(4));
                Team roadTeam = teamMap.get(rs.getInt(5));
                int homeScore = rs.getInt(6);
                int roadScore = rs.getInt(7);
                String league = rs.getString(8);
                events.add(new Event(eventID,stadium,cal,homeTeam,roadTeam,homeScore,roadScore,league));

            }
            DbConn.close();
        }catch (Exception e){
            Log.e("Db aEFU",e.toString());
            return events;
        }
        return events;
    }
}
