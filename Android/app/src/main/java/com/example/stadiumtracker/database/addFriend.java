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

public class addFriend extends AsyncTask<String, Void, Integer> {

    private String ip;
    private String port;
    private String dbName;
    private String user;
    private String pass;

    private Context context;

    public addFriend(Context context){
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
        try{
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
            PreparedStatement ps = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[User] WHERE Username = ?");
            ps.setString(1,strings[0]);
            ResultSet rs = ps.executeQuery();
            if(rs.next() == false){//No user matches that name
                DbConn.close();
                return -1;
            }
            //Check if there is already a pending friend request between the 2 users, if there is return 1 else return 0 and add an entry into the friends table
            //Where the first param from String... strings is UserID2 and 2nd param is UserID1
            else{
                int userID = rs.getInt(1);
                PreparedStatement ps2 = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Friends] WHERE ((UserID1 = ?) AND (UserID2 = ?)) OR ((UserID1 = ?) AND (UserID2=?))");
                ps2.setInt(1,userID);
                ps2.setInt(2,Integer.parseInt(strings[1]));
                ps2.setInt(3,Integer.parseInt(strings[1]));
                ps2.setInt(4,userID);
                ResultSet rs2 = ps2.executeQuery();
                if(rs2.next() == false){//Need to add a new entry into friend table
                    PreparedStatement ps3 = DbConn.prepareStatement("INSERT INTO Friends (UserID1,UserID2,Start_Date,User1Accept,User2Accept) VALUES (?,?,0,?,?)");
                    ps3.setInt(1,Integer.parseInt(strings[1]));
                    ps3.setInt(2,userID);
                    ps3.setInt(3,1);
                    ps3.setInt(4,0);
                    ps3.execute();
                    DbConn.close();
                    return 0;

                }
                else{//This friendship already exists
                    DbConn.close();
                    return 1;
                }
            }
        } catch (Exception e){
            Log.w("Error add friend query", "" + e);
            return -2;
        }

    }
}
