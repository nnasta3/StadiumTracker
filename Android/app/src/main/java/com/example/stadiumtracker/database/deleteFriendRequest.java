package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.stadiumtracker.R;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class deleteFriendRequest extends AsyncTask<Integer, Void, Void> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* NICHOLAS NASTA
     * Constructor, sets strings for the database connection
     */
    public deleteFriendRequest(Context context){
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
     * Deletes the friend request between the sending user and receiving user
     */
    @Override
    protected Void doInBackground(Integer... integers) {
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("DELETE FROM [dbo].[Requests] WHERE Sender = ? AND Receiver = ?");
            ps.setInt(1,integers[0]);
            ps.setInt(2,integers[1]);
            ps.executeUpdate();
            DbConn.close();
        } catch (Exception e) {
            Log.e("Error deleteFriendRequest", "" + e);
            return null;
        }
        return null;
    }
}
