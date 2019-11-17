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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    EditText confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = (EditText) findViewById(R.id.username_sign_up);
        passwordField = (EditText) findViewById(R.id.password_sign_up);
        confirmPasswordField = (EditText) findViewById(R.id.password_confirm_sign_up);
    }

    public void signUpHandler(View v){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if(validateFields(username,password,confirmPassword)){
            //INSERT INTO [User](Username,Password) VALUES (username,password);
            try{
                if(new InsertUser().execute(username,password).get()){
                    //insertion complete send user to login
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    //insertion failed display message
                    Toast.makeText(getApplicationContext(),"Sign up failed.",Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                Log.w("Error insertion", "" + e);
            }

        }

    }
    private boolean validateFields(String username, String password, String confirmPassword){
        int userLengthMin = 3;
        int passLengthMin = 3;
        //Username length minimum
        if(username.length() < userLengthMin){
            Toast.makeText(getApplicationContext(),"Username must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //username taken
        try{
            if(new UsernameQuery().execute(username).get()){
                Toast.makeText(getApplicationContext(),"Username must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(Exception e) {
            Log.w("Error validateFields", "" + e);
        }
        //Password length minimum
        if(password.length() < passLengthMin){
            Toast.makeText(getApplicationContext(),"Password must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Passowrds dont match
        if(!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(),"Passwords do not match.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    class  UsernameQuery extends AsyncTask<String, Void, Boolean> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Boolean doInBackground(String... strings) {
            //Strings[0] = username
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select Username from [User] where Username='"+strings[0]+"'");
                if(rs.next()){
                    return true;
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error connection", "" + e);
                return false;
            }
            return false;
        }
    }
    class  InsertUser extends AsyncTask<String, Void, Boolean> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Boolean doInBackground(String... strings) {
            //Strings[0] = username
            //String[1] = password
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [User](Username,Password) VALUES (?,?);");
                ps.setString(1,strings[0]);
                ps.setString(2,strings[1]);
                ps.executeUpdate();
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error connection", "" + e);
                return false;
            }
            return true;
        }
    }
}
