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
import java.util.HashMap;
import java.util.Map;

public class stadiumsCountForUser extends AsyncTask<Integer, Void, Map<Integer,Integer>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* John Strauser
        constructor for stadiumsCountForUser query class
        context is used in SetStrings
     */
    public stadiumsCountForUser(Context context){
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
        takes a userID as input
        returns a hashmap of integers
        The map uses a stadiumID as the key and the number of visits to that stadium for the userID as the value
     */
    @Override
    protected Map<Integer, Integer> doInBackground(Integer... integers) {
        //integers[0] = userID
        Map<Integer, Integer> out = new HashMap<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement stmt = DbConn.prepareStatement("SELECT StadiumID, Count(*) AS 'count' FROM [stadiumTrackerDB].[dbo].[Event] WHERE EventID IN (SELECT EventID FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID = ?) GROUP BY StadiumID");
            stmt.setInt(1,integers[0]);
            ResultSet rs = stmt.executeQuery();
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
