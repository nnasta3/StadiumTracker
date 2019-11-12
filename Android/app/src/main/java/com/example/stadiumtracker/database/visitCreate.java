package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class  visitCreate extends AsyncTask<String, Void, Boolean> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public visitCreate(Context context){
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
    protected Boolean doInBackground(String... strings) {
        //Strings[0] = eventID
        //Strings[1] = visitID
        int out = -1;
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [Visit](EventID,UserID) VALUES (convert(int,?),convert(int,?));");
            ps.setString(1,strings[0]);
            ps.setString(2,strings[1]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error visit create", "" + e);
            return false;
        }
        return true;
    }
}
