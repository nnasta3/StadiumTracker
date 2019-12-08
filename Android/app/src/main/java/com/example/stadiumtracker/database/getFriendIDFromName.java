package com.example.stadiumtracker.database;

import android.content.Context;
import com.example.stadiumtracker.R;
import android.os.AsyncTask;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class getFriendIDFromName extends AsyncTask<String, Void, Integer> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* NICHOLAS NASTA
     * Constructor, sets strings for the database connection
     */
    public getFriendIDFromName(Context context){
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
     * Pulls the UserID of the given friend name
     * returns the UserID of the friend if found
     * returns -1 if an error or not found
     */
    @Override
    protected Integer doInBackground(String... strings) {
        int friendID = -1;
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT UserID FROM [stadiumTrackerDB].[dbo].[User] WHERE Username = ?");
            ps.setString(1, strings[0]);
            ResultSet rs = ps.executeQuery();
            rs.next();
            friendID = rs.getInt(1);
            return  friendID;
        }
        catch(Exception e){
            Log.w("Error getFriendIDFromName query", "" + e);
        }

        return friendID;
    }
}
