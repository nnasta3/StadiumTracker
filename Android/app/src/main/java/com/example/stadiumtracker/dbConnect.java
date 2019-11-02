package com.example.stadiumtracker;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class  dbConnect extends AsyncTask<String, Void, ResultSet> {
    ResultSet set = null;
    String ip = "database-2.ctqwj4cnvuoo.us-east-2.rds.amazonaws.com" ;
    String port = "1433";
    String dbName = "test";
    String masterUser = "admin";
    String masterPass = "password";
    @Override
    protected ResultSet doInBackground(String... strings) {
        try {
            // SET CONNECTIONSTRING
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ ip + ":" + port +"/" + dbName + ";user=" + masterUser + ";password=" + masterPass);
            Statement stmt = DbConn.createStatement();
            if (strings.length == 1){
                Log.w("dbConnect execute",strings[0]);
                set = stmt.executeQuery(strings[0]);

            }
            printSet();
            DbConn.close();
            printSet();
        } catch (Exception e)
        {
            Log.w("Error connection","" + e);
        }
        return set;
    }
    private void printSet(){
        try{
            if(!set.isBeforeFirst()){
                set.beforeFirst();
            }
            while(set.next()){
                Log.w("printSet printing","" + set.getString(1));
            }
        }catch(Exception e){
            Log.w("Error printSet","" + e);
        }
    }
}
