package com.example.stadiumtracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stadiumtracker.database.insertUser;
import com.example.stadiumtracker.database.usernameQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    EditText confirmPasswordField;
    /*John Strauser
        Initializes the UI
           Uses findViewByID to get UI components
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = (EditText) findViewById(R.id.username_sign_up);
        passwordField = (EditText) findViewById(R.id.password_sign_up);
        confirmPasswordField = (EditText) findViewById(R.id.password_confirm_sign_up);
    }
    /*John Strauser
        Called when signup button is clicked
        Gets information from UI form
        calls validate fields
        if fields are valid, attempt to create account
        if account creation fails, tell user. Otherwsie, send user to loginactivity
     */
    public void signUpHandler(View v){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if(validateFields(username,password,confirmPassword)){
            //INSERT INTO [User](Username,Password) VALUES (username,password);
            try{
                if(new insertUser(this).execute(username,password).get()){
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
    /*John Strauser
        Check that username and password fields are valid using if statements
        display failure reason and return false if a field is invalid
     */
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
            if(new usernameQuery(this).execute(username).get()){
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


}
