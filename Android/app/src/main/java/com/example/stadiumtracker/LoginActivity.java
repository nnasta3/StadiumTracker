package com.example.stadiumtracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         usernameField = (EditText) findViewById(R.id.username_login);
         passwordField = (EditText) findViewById(R.id.password_login);

    }

    public void loginHandler(View v){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.length() <= 0){
            Toast.makeText(getApplicationContext(),"Please Enter A Username",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() <= 0){
            Toast.makeText(getApplicationContext(),"Please Enter A Password",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userID = new loginQuery().execute(username,password).get();
            if(userID != -1){
                Intent intent = new Intent(this,MainMenuActivity.class);
                intent.putExtra("UserID",userID);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.w("Error","" + e);
        }

    }

    public void registerHandler(View v){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
    class  loginQuery extends AsyncTask<String, Void, Integer> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Integer doInBackground(String... strings) {
            //Strings[0] = username/email
            //Strings[1] = password
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select Username,Password,UserID from [User] where Password='"+strings[1]+"'");
                while(rs.next()){
                    String user = rs.getString(1);
                    String email = rs.getString(2);
                    if(strings[0].equals(user) || strings[0].equals(email)){
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
}
