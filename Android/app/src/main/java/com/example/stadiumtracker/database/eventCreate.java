package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class  eventCreate extends AsyncTask<String, Void, Integer> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public eventCreate(Context context){
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
    protected Integer doInBackground(String... strings) {
        //Strings[0] = stadiumID
        //Strings[1] = date
        //Strings[2] = home team
        //Strings[3] = road team
        //Strings[4] = home score
        //Strings[5] = road score
        //Strings[6] = league
        int out = -1;
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [Event](StadiumID,Date,Home_TeamID,Road_TeamID,Home_Score,Away_Score,League) VALUES (convert(int,?),convert(date,?),convert(int,?),convert(int,?),convert(int,?),convert(int,?),?);");
            ps.setString(1,strings[0]);
            ps.setString(2,strings[1]);
            ps.setString(3,strings[2]);
            ps.setString(4,strings[3]);
            ps.setString(5,strings[4]);
            ps.setString(6,strings[5]);
            ps.setString(7,strings[6]);
            ps.executeUpdate();

            //Query the database for the eventID to return
            Statement stmt = DbConn.createStatement();
            ResultSet rs = stmt.executeQuery("Select EventID from [Event] where StadiumID="+strings[0]+" AND Date='"+strings[1]+"' AND Home_TeamID="+strings[2]+" AND Road_TeamID="+strings[3]+" AND Home_Score="+strings[4]+" AND Away_Score="+strings[5]+" AND League='"+strings[6]+"'");
            rs.next();
            out = rs.getInt(1);
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error event create", "" + e);
            return out;
        }
        return out;
    }
}
