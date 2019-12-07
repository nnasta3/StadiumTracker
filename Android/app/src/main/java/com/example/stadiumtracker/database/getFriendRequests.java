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

public class getFriendRequests extends AsyncTask<Integer, Void, ArrayList<String>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public getFriendRequests(Context context){
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
    protected ArrayList<String> doInBackground(Integer... integers) {
        ArrayList<String> retList = new ArrayList<String>();
        try{
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT Sender FROM [stadiumTrackerDB].[dbo].[Requests] WHERE Receiver = ?");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            //Convert the senderIDs into sender names and return the list
            while (rs.next()) {
                int senderID = rs.getInt(1);

                PreparedStatement ps2 = DbConn.prepareStatement("SELECT Username FROM [stadiumTrackerDB].[dbo].[User] WHERE UserID = ?");
                ps2.setInt(1,senderID);

                ResultSet rs2 = ps2.executeQuery();
                rs2.next();
                String name = rs2.getString(1);
                retList.add(name);
            }

            DbConn.close();
            return retList;

        } catch (Exception e){
            Log.w("Error add friend query", "" + e);
            return retList;
        }
    }
}
