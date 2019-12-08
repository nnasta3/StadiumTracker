package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class  allStadiums extends AsyncTask<String, Void, List<Stadium>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;
    /* John Strauser
        constructor for allStadiums query class
        context is used in setStrings
     */
    public allStadiums(Context context){
        //Log.e("allStadiums","constructor");
        this.context = context;
        setStrings();
        //Log.e("allStadiums","done");
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
        no inputs needed
        returns a list of all stadium instances found in the database
     */
    @Override
    protected List<Stadium> doInBackground(String... strings) {
        //Log.e("allStadiums","starting doinb");
        List<Stadium> out = new ArrayList<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("Select * from [Stadium] Order By Name");
            //Log.e("allStadiums","prepared");
            ResultSet rs = ps.executeQuery();
            //Log.e("allStadiums","executed. now looping");
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String city = rs.getString(3);
                String country = rs.getString(4);
                double gpsLat = rs.getDouble(5);
                double gpsLong = rs.getDouble(6);
                String url = rs.getString(7);
                out.add(new Stadium(id,name,city,country,gpsLat,gpsLong,url));
            }
            //Log.e("allStadiums","loop complete");
            DbConn.close();
        } catch (Exception e) {
            Log.e("Error all stadiums", "" + e);
            return out;
        }
        //Log.e("allStadiums","returning");
        return out;
    }
}
