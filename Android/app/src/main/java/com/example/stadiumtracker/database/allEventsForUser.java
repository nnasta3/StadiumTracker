package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class allEventsForUser extends AsyncTask<Integer, Void, List<Event>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public allEventsForUser(Context context){
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
    protected List<Event> doInBackground(Integer... integers) {
        //integers[0] = userID
        List<Event> events = new ArrayList<>();
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [dbo].[Event] WHERE EventID IN (SELECT EventID FROM [dbo].[Visit] WHERE UserID=?) ORDER BY Date DESC");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int eventID = rs.getInt(1);
                int stadiumID = rs.getInt(2);
                Date date = rs.getDate(3);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int homeID = rs.getInt(4);
                int roadID = rs.getInt(5);
                PreparedStatement stmtHome = DbConn.prepareStatement("SELECT Nickname FROM [dbo].[Team] WHERE TeamID=?");
                PreparedStatement stmtRoad = DbConn.prepareStatement("SELECT Nickname FROM [dbo].[Team] WHERE TeamID=?");
                stmtHome.setInt(1,homeID);
                stmtRoad.setInt(1,roadID);
                ResultSet rsHome = stmtHome.executeQuery();
                ResultSet rsRoad = stmtRoad.executeQuery();
                rsHome.next();
                rsRoad.next();
                String homeTeam = rsHome.getString(1);
                String roadTeam = rsRoad.getString(1);
                int homeScore = rs.getInt(6);
                int roadScore = rs.getInt(7);
                String league = rs.getString(8);
                events.add(new Event(eventID,stadiumID,cal,homeTeam,roadTeam,homeScore,roadScore,league));

            }
        }catch (Exception e){
            Log.e("Db aEFU",e.toString());
            return events;
        }
        return events;
    }
}
