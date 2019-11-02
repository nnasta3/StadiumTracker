package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

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

        try{
            ResultSet set = new dbConnect().execute("select username from users").get();
            int valid = 0;
            while(set.next()){
                if(set.getString(1).equals(username)){
                    Toast.makeText(getApplicationContext(),"valid login",Toast.LENGTH_SHORT).show();
                    //TODO: Replace the valid thing with an intent switch
                    return;
                }
            }
            if(valid == 0){
                Toast.makeText(getApplicationContext(),"This would be an invalid login",Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException e){
            Log.w("Error","" + e);
            Toast.makeText(getApplicationContext(),"This would be an invalid login error",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.w("Error","" + e);
        }

    }

    public void registerHandler(View v){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
}
