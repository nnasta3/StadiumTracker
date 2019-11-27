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

public class loginQuery extends AsyncTask<String, Void, Integer> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public loginQuery(Context context){
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
        protected Integer doInBackground(String... strings) {
        //Strings[0] = username/email
        //Strings[1] = password
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement stmt = DbConn.prepareStatement("Select Username,Password,UserID from [User] where Password=?");
            stmt.setString(1,strings[1]);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String user = rs.getString(1);
                if(strings[0].equals(user)){
                    return rs.getInt(3);
                }
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error connection", "" + e);
        }
        return -1;
    }
}
