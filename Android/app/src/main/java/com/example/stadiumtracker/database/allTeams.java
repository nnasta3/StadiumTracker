package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.Team;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class allTeams extends AsyncTask<Void, Void, List<Team>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;
    private Map<Integer,Stadium> stadiumMap;
    private Context context;
    /* John Strauser
        constructor for allTeams query class
        context is used in setStrings
     */
    public allTeams(Context context){
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
        onPreExecute runs after allTeams.execute() is called and before doInBackground() is called
        creates a hash map of stadiums to reduce the number of queries needed
        stadium map uses the stadiumID as a key and the stadium instance as the value
     */
    @Override
    protected void onPreExecute(){
        try{
            List<Stadium> stadiums = new allStadiums(context).execute().get();
            stadiumMap = new HashMap<>();
            for(Stadium stadium : stadiums){
                stadiumMap.put(stadium.getStadiumID(),stadium);
            }
        }catch (Exception e){
            Log.e("allTeams",e.toString());
        }
    }
    /* John Strauser
        no inputs needed
        returns a list of team instances that can be found in the database
     */
    @Override
    protected List<Team> doInBackground(Void... voids) {
        List<Team> teams = new ArrayList<>();
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [dbo].[Team]");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int teamID = rs.getInt(1);
                String city = rs.getString(2);
                String nickname = rs.getString(3);
                String abbrev = rs.getString(4);
                Stadium stadium = stadiumMap.get(rs.getInt(5));
                String league = rs.getString(6);
                teams.add(new Team(teamID,city,nickname,abbrev,stadium,league));
            }
        }catch (Exception e){
            Log.e("allTeams",e.toString());
            return teams;
        }
        return teams;
    }
}
