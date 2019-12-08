package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.loginQuery;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;

    /* John Strauser
        Initializes the UI
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         usernameField = (EditText) findViewById(R.id.username_login);
         passwordField = (EditText) findViewById(R.id.password_login);

    }
    /* John Strauser
        Called on click of "login" button
        Takes username and password from text boxes in UI
        Ensures information input is valid
        Performs login query
        Sends user to main menu if loginQuery returns success, otherwise displays "invalid login" message
     */
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
            int userID = new loginQuery(this).execute(username,password).get();
            if(userID != -1) {
                User user = new User(userID,username);
                Intent intent = new Intent(this,MainMenuActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Invalid Login",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.w("Error","" + e);
        }

    }
    /* John Strauser
        Called when register button is clicked
        sends user to sign up activity
     */
    public void registerHandler(View v){
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

}
