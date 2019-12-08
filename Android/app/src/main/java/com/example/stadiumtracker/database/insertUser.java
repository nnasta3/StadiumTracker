package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class  insertUser extends AsyncTask<String, Void, Boolean> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* John Strauser
        constructor for the insertUser query class
        context is used in SetStrings
     */
    public insertUser(Context context){
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
        Takes a username and password as input
        insert the new user into the database
        return true if insertion was successful, false otherwise
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        //Strings[0] = username
        //String[1] = password
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [User](Username,Password) VALUES (?,?);");
            ps.setString(1,strings[0]);
            ps.setString(2,strings[1]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error connection", "" + e);
            return false;
        }
        return true;
    }
}
