package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;
import com.example.stadiumtracker.data.Stadium;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class getStadium extends AsyncTask<Integer, Void, Stadium> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;
    /* John Strauser
        constructor for the getStadium query class
        context is used in SetStrings
     */
    public getStadium(Context context){
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
        takes a stadiumID as input
        returns a new instance of the stadium class from the query
     */
    @Override
    protected Stadium doInBackground(Integer... integers) {
        //integers[0] = stadiumID
        Stadium stadium = null;
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("Select * from [Stadium] WHERE stadiumID=?");
            ps.setInt(1,integers[0]);
            ResultSet rs = ps.executeQuery();
            rs.next();
            stadium = new Stadium(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDouble(5),rs.getDouble(6),rs.getString(7));
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error all stadiums", "" + e);
            return stadium;
        }

        return stadium;
    }
}
