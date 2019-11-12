package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;

import java.sql.Connection;
import java.sql.DriverManager;
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

    public allStadiums(Context context){
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
    protected List<Stadium> doInBackground(String... strings) {
        List<Stadium> out = new ArrayList<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            Statement stmt = DbConn.createStatement();
            ResultSet rs = stmt.executeQuery("Select StadiumID,Name,City,Country,Gps_Lat,Gps_Long from [Stadium] Order By Name");
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String city = rs.getString(3);
                String country = rs.getString(4);
                double gpsLat = rs.getDouble(5);
                double gpsLong = rs.getDouble(6);
                out.add(new Stadium(id,name,city,country,gpsLat,gpsLong));
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error all stadiums", "" + e);
            return out;
        }
        return out;
    }
}
