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
import java.util.ArrayList;
import java.util.List;

public class  allLeagues extends AsyncTask<String, Void, List<String>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;
    /* John Strauser
        constructor for allLeagues query class
        context is used for setStrings
     */
    public allLeagues(Context context){
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
        no inputs needed
        returns a list of league names
        example output would be {"NFL","NBA","MLB"}
     */
    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> out = new ArrayList<>();
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("Select Abbrev from [League] Order By Abbrev");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String name = rs.getString(1);
                out.add(name);
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error all leagues", "" + e);
            return out;
        }
        return out;
    }
}
