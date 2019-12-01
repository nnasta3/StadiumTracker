package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.stadiumtracker.R;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class friendsList extends AsyncTask<Integer, Void, List<Map<String, Date>>> {
    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public friendsList(Context context){
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
    protected List<Map<String, Date>> doInBackground(Integer... integers) {
        List<Map<String, Date>> retList = new ArrayList<>();

        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Friends] WHERE (USERID1 = ?) OR (USERID2 = ?)");
            ps.setInt(1,integers[0]);
            ps.setInt(2,integers[0]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                //Find which UserID is not the current user and then map it
                int userID1 = rs.getInt(1);
                int userID2 = rs.getInt(2);
                Date date = rs.getDate(3);
                int user1Accept = rs.getInt(4);
                int user2Accept = rs.getInt(5);

                //Convert UserID to Username
                if (userID1 == integers[0] && user2Accept == 1 && user1Accept ==1) {
                    PreparedStatement ps2 = DbConn.prepareStatement("SELECT Username FROM [stadiumTrackerDB].[dbo].[User] WHERE UserID = ?");
                    ps2.setInt(1,userID2);
                    ResultSet rs2 = ps2.executeQuery();
                    rs2.next();
                    String name = rs2.getString(1);
                    Map<String,Date> temp = new HashMap<String,Date>();
                    temp.put(name,date);
                    retList.add(temp);
                }
                else {
                    if(user1Accept == 1 && user2Accept ==1) {
                        PreparedStatement ps2 = DbConn.prepareStatement("SELECT Username FROM [stadiumTrackerDB].[dbo].[User] WHERE UserID = ?");
                        ps2.setInt(1, userID1);
                        ResultSet rs2 = ps2.executeQuery();
                        rs2.next();
                        String name = rs2.getString(1);
                        Map<String, Date> temp = new HashMap<String, Date>();
                        temp.put(name, date);
                        retList.add(temp);
                    }
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
