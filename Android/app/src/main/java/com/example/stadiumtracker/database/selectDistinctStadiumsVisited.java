package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class selectDistinctStadiumsVisited extends AsyncTask<Integer, Void, ArrayList<Integer>> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public selectDistinctStadiumsVisited(Context context){
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
    protected ArrayList<Integer> doInBackground(Integer... integers) {
        ArrayList<Integer> retList = new ArrayList<Integer>();
        try{
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT distinct StadiumID FROM [stadiumTrackerDB].[dbo].[Event] WHERE EventID IN (SELECT EventID FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID= ?)");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            //Get the distinct stadiums a user has visited
            while (rs.next()) {
                int stadiumID = rs.getInt(1);
                retList.add(stadiumID);

            }

            DbConn.close();
            return retList;

        } catch (Exception e){
            Log.w("Error add friend query", "" + e);
            return retList;
        }
    }
}
