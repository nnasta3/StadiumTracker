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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  eventsQuery extends AsyncTask<String, Void, Event> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;
    private Map<Integer, Stadium> stadiumMap;
    private Map<Integer, Team> teamMap;
    private Context context;

    public eventsQuery(Context context){
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
            //Get lists of stadiums and teams
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
            Log.e("eQ","onPreExecute");
        }

    }
    @Override
    protected Event doInBackground(String... strings) {
        //Strings[0] = stadiumID
        //Strings[1] = date
        //Strings[2] = home team
        //Strings[3] = road team
        //Strings[4] = home score
        //Strings[5] = road score
        //Strings[6] = league
        Event out;
        try {

            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("Select * from [Event] where StadiumID=convert(int,?) AND Date=convert(date,?) AND Home_TeamID=convert(int,?) AND Road_TeamID=convert(int,?) AND Home_Score=convert(int,?) AND Away_Score=convert(int,?) AND League=?");
            ps.setString(1,strings[0]);
            ps.setString(2,strings[1]);
            ps.setString(3,strings[2]);
            ps.setString(4,strings[3]);
            ps.setString(5,strings[4]);
            ps.setString(6,strings[5]);
            ps.setString(7,strings[6]);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
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
                out = new Event(eventID,stadium, cal, homeTeam,roadTeam,homeScore,roadScore,league);
            }else{
                return null;
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error events query", "" + e);
            return null;
        }
        return out;
    }
}
