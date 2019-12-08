package com.example.stadiumtracker.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.stadiumtracker.R;
import java.sql.Connection;
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

    /* NICHOLAS NASTA
     * Constructor, sets strings for the database connection
     */
    public addFriend(Context context){
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
     * Check if there is already a pending friend request between the 2 users, if there is return 1 else return 0 and add an entry into the friends table
     * Where the first param from String... strings is UserID2 and 2nd param is UserID1
     * returns -1 for no existing user to add as a friend in the database
     * returns -3 trying to add themselves as a friend
     * returns 1 if the user is already friends with who they want to add
     * returns 0 if the user successfully added a friend
     * returns -2 for database error
     */
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

                //User is trying to add themselves as a friend
                if(userID == Integer.parseInt(strings[1])){
                    DbConn.close();
                    return -3;
                }
                //Check if the users are already friends
                PreparedStatement ps4 = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Friends] WHERE ((User1 = ?) AND (User2 = ?)) OR ((User1 = ?) AND (User2=?))");
                ps4.setInt(1,userID);
                ps4.setInt(2,Integer.parseInt(strings[1]));
                ps4.setInt(3,Integer.parseInt(strings[1]));
                ps4.setInt(4,userID);
                ResultSet rs4 = ps4.executeQuery();
                if(rs4.next()!=false){
                    return 1;
                }

                PreparedStatement ps2 = DbConn.prepareStatement("SELECT * FROM [stadiumTrackerDB].[dbo].[Requests] WHERE ((Sender = ?) AND (Receiver = ?)) OR ((Sender = ?) AND (Receiver=?))");
                ps2.setInt(1,userID);
                ps2.setInt(2,Integer.parseInt(strings[1]));
                ps2.setInt(3,Integer.parseInt(strings[1]));
                ps2.setInt(4,userID);
                ResultSet rs2 = ps2.executeQuery();

                //Need to add a new entry into requests table
                if(rs2.next() == false){
                    PreparedStatement ps3 = DbConn.prepareStatement("INSERT INTO [stadiumTrackerDB].[dbo].[Requests] (Sender,Receiver) VALUES (?,?)");
                    ps3.setInt(1,Integer.parseInt(strings[1]));
                    ps3.setInt(2,userID);
                    ps3.execute();
                    DbConn.close();
                    return 0;

                }
                //This friendship already exists
                else{
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
