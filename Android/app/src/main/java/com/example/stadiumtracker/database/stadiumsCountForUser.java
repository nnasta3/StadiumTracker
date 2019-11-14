package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Event;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class stadiumsCountForUser extends AsyncTask<Integer, Void, Map<Integer,Integer>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public stadiumsCountForUser(Context context){
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
    protected Map<Integer, Integer> doInBackground(Integer... integers) {
        //integers[0] = userID
        Map<Integer, Integer> out = new HashMap<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            Statement stmt = DbConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT StadiumID, Count(*) AS 'count' FROM [stadiumTrackerDB].[dbo].[Event] WHERE EventID IN (SELECT EventID FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID = "+integers[0]+") GROUP BY StadiumID");
            while (rs.next()){
                int stadiumID = rs.getInt(1);
                int count = rs.getInt(2);
                out.put(stadiumID,count);
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error events query", "" + e);
            return out;
        }
        return out;
    }
}
