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
import java.util.List;

public class getFriendViewDetails extends AsyncTask<Integer, Void, ArrayList<Integer>> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public getFriendViewDetails(Context context){
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
        ArrayList<Integer> retList = new ArrayList<>();

        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT count(Distinct StadiumID) FROM [stadiumTrackerDB].[dbo].[Event] WHERE EventID IN (SELECT EventID FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID= ?)");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            rs.next();

            //Get Stadiums Visited Count
            int stadiumsCount = rs.getInt(1);
            retList.add(stadiumsCount);

            PreparedStatement ps2 = DbConn.prepareStatement("SELECT count(UserID) FROM [stadiumTrackerDB].[dbo].[Visit] WHERE UserID = ?");
            ps2.setInt(1,integers[0]);
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();

            //Get visits Count
            int visitsCount = rs2.getInt(1);
            retList.add(visitsCount);

            DbConn.close();

        } catch (Exception e) {
            Log.w("Error friends list query", "" + e);
            return retList;
        }
        return retList;
    }
}
