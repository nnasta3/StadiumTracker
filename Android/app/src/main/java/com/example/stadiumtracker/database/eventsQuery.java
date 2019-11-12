package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class  eventsQuery extends AsyncTask<String, Void, Event> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

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
            Statement stmt = DbConn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from [Event] where StadiumID="+strings[0]+" AND Date='"+strings[1]+"' AND Home_TeamID="+strings[2]+" AND Road_TeamID="+strings[3]+" AND Home_Score="+strings[4]+" AND Away_Score="+strings[5]+" AND League='"+strings[6]+"'");
            if(rs.next()){
                int eventID = rs.getInt(1);
                int stadiumID = rs.getInt(2);
                Date date = rs.getDate(3);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                String homeTeam = rs.getString(4);
                String roadTeam = rs.getString(5);
                int homeScore = rs.getInt(6);
                int roadScore = rs.getInt(7);
                String league = rs.getString(8);
                out = new Event(eventID,stadiumID, cal, homeTeam,roadTeam,homeScore,roadScore,league);
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
