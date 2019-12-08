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

public class  usernameQuery extends AsyncTask<String, Void, Boolean> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* John Strauser
        Constructor for the usernameQuery query class
        Context is used in setStrings
     */
    public usernameQuery(Context context){
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
        takes a username as input
        returns true is the username exists in the database, false otherwise
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        //Strings[0] = username
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement stmt = DbConn.prepareStatement("Select Username from [User] where Username=?");
            stmt.setString(1,strings[0]);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error connection", "" + e);
            return false;
        }
        return false;
    }
}