package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class  visitCreate extends AsyncTask<Integer, Void, Boolean> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* John Strauser
        Constructor for the visitCreate query class
        Context is used to get the database information
     */
    public visitCreate(Context context){
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
        Takes an eventID and userID as inputs
        Creates an entry in the visit table for the input eventID and userID
        returns true if the insertion was successful, false otherwise
     */
    @Override
    protected Boolean doInBackground(Integer... ints) {
        //ints[0] = eventID
        //ints[1] = userID
        int out = -1;
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [Visit](EventID,UserID) VALUES (?,?);");
            ps.setInt(1,ints[0]);
            ps.setInt(2,ints[1]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error visit create", "" + e);
            return false;
        }
        return true;
    }
}
