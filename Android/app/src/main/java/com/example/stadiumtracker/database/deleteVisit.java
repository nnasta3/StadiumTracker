package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class deleteVisit extends AsyncTask<Integer, Void, Void> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;
    /* John Strauser
        constructor for the deleteVisit query class
        context is used in setStrings
     */
    public deleteVisit(Context context){
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
        Takes a userID and EventID as input
        Delete the visit from the visit table
        always return null
     */
    @Override
    protected Void doInBackground(Integer... integers) {
        //integers[0] = userID
        //integers[1] = eventID
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("DELETE FROM [dbo].[Visit] WHERE UserID = ? AND EventID = ?");
            ps.setInt(1,integers[0]);
            ps.setInt(2,integers[1]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.e("Error delete visit", "" + e);
            return null;
        }
        return null;
    }
}
