package com.example.stadiumtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         usernameField = (EditText) findViewById(R.id.username);
         passwordField = (EditText) findViewById(R.id.password);
    }

    public void loginHandler(View v){
        //TODO: query datatbase for usernames and emails where password = text field
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
        Toast.makeText(getApplicationContext(),"Query Database would occur here",Toast.LENGTH_SHORT).show();
    }

    public void registerHandler(View v){
        //TODO: send to signup activity
        Toast.makeText(getApplicationContext(),"Send to register page would occur here",Toast.LENGTH_SHORT).show();
    }
}
