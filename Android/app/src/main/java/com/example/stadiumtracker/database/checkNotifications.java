package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.stadiumtracker.R;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class checkNotifications extends AsyncTask<Integer, Void, Integer> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* NICHOLAS NASTA
     * Constructor, sets strings for the database connection
     */
    public checkNotifications(Context context){
        super();
        this.context = context;
        setStrings();
    }

    /* NICHOLAS NASTA
     * Sets the string values for the database connection
     */
    private void setStrings(){
        ip = context.getResources().getString(R.string.ip);
        port = context.getResources().getString(R.string.port);
        dbName = context.getResources().getString(R.string.db_name);
        user = context.getResources().getString(R.string.masterUser);
        pass = context.getResources().getString(R.string.masterPass);
    }

    /* NICHOLAS NASTA
     * Connects to the database
     * Pulls a list of friend requests send to the current user
     * returns -2 database connection error
     * returns 1 if there are no pending notifications
     * returns 0 if there are pending notifications
     */
    @Override
    protected Integer doInBackground(Integer... integers) {
        try{
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Requests] WHERE Receiver = ?");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            if(rs.next() == false){//No Notifications
                DbConn.close();
                return 0;
            }
            else{//There are pending notifications
                DbConn.close();
                return 1;
            }
        } catch (Exception e){
            Log.w("Error add friend query", "" + e);
            return -2;
        }
    }
}
