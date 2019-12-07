package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stadiumtracker.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class removeFriend extends AsyncTask<Integer, Void, Void> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public removeFriend(Context context){
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
    protected Void doInBackground(Integer... integers) {
        //integers[0] = userID
        //integers[1] = eventID
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("DELETE FROM [dbo].[Friends] WHERE (User1 = ? AND User2 = ?) OR (User1 = ? AND User2 = ?)");
            ps.setInt(1,integers[0]);
            ps.setInt(2,integers[1]);
            ps.setInt(3,integers[1]);
            ps.setInt(4,integers[0]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.e("Error delete friend", "" + e);
            return null;
        }
        return null;
    }
}
