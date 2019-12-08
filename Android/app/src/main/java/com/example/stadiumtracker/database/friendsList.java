package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.stadiumtracker.R;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class friendsList extends AsyncTask<Integer, Void, List<String>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    /* NICHOLAS NASTA
     * Constructor, sets strings for the database connection
     */
    public friendsList(Context context){
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
     * Pull the list of friends for the current user
     * Find which UserID is not the current user
     * Convert UserID to Username
     * Add the username of the friend to the retList
     * return retList
     * returns null List<String> if there was an error
     */
    @Override
    protected List<String> doInBackground(Integer... integers) {
        List<String> retList = new ArrayList<>();

        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Friends] WHERE (USER1 = ?) OR (USER2 = ?)");
            ps.setInt(1,integers[0]);
            ps.setInt(2,integers[0]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //Find which UserID is not the current user
                int userID1 = rs.getInt(1);
                int userID2 = rs.getInt(2);

                //Convert UserID to Username
                if (userID1 == integers[0] ) {
                    PreparedStatement ps2 = DbConn.prepareStatement("SELECT Username FROM [stadiumTrackerDB].[dbo].[User] WHERE UserID = ?");
                    ps2.setInt(1,userID2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    String name = rs2.getString(1);
                    retList.add(name);
                }
                else {
                    PreparedStatement ps2 = DbConn.prepareStatement("SELECT Username FROM [stadiumTrackerDB].[dbo].[User] WHERE UserID = ?");
                    ps2.setInt(1, userID1);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    String name = rs2.getString(1);
                    retList.add(name);

                }
            }
            DbConn.close();
        } catch (Exception e) {
            Log.w("Error friends list query", "" + e);
            return retList;
        }
        return retList;
    }
}
