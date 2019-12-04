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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class VisitsForUserToStadium extends AsyncTask<Integer, Void, List<Event>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;
    private Map<Integer, Stadium> stadiumMap;
    private Map<Integer, Team> teamMap;
    private Context context;

    public VisitsForUserToStadium
            (Context context){
        super();
        this.context = context;
        setStrings();
    }

    private void setStrings(){
        ip = context.getResources().getString(R.string.ip);
        port = context.getResources().getString(R.string.port);
        dbName = context.getResources().getString(R.string.db_name);
        user = context.getResources().getString(R.string.masterUser);
        pass = context.getResources().getString(R.string.masterPass);
    }
    @Override
    protected void onPreExecute(){
        try{
            List<Stadium> stadiums = new allStadiums(context).execute().get();
            List<Team>  teams = new allTeams(context).execute().get();
            //Create map for stadiums and teams
            stadiumMap = new HashMap<>();
            teamMap = new HashMap<>();
            for (Stadium stadium : stadiums){
                stadiumMap.put(stadium.getStadiumID(),stadium);
            }
            for (Team team : teams){
                teamMap.put(team.getTeamID(),team);
            }
        }catch (Exception e){
            Log.e("VFUTS",e.toString());
        }
    }
    @Override
    protected List<Event> doInBackground(Integer... integers) {
        //integers[0] = userID
        //integers[1] = stadiumID
        List<Event> out = new ArrayList<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement stmt = DbConn.prepareStatement("SELECT * FROM [dbo].[Event] WHERE EventID IN (SELECT EventID FROM [dbo].[Visit] WHERE UserID=?) AND StadiumID=? ORDER BY Date DESC");
            stmt.setInt(1,integers[0]);
            stmt.setInt(2,integers[1]);
            ResultSet rs = stmt.executeQuery();
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
                out.add(new Event(eventID,stadium,cal,homeTeam,roadTeam,homeScore,roadScore,league));
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error visits query", "" + e);
            return out;
        }
        return out;

    }
}
