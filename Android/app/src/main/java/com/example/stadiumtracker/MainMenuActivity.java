package com.example.stadiumtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userID = savedInstanceState.getInt("userID");
        Toast.makeText(getApplicationContext(),"id: "+userID,Toast.LENGTH_SHORT).show();
    }

    public void recordHandler(View v){
        //TODO: Send to record page
    }
    public void stadiumHandler(View v){
        //TODO: Send to stadium page
    }
    public void visitHandler(View v){
        //TODO: Send to visit page
    }
    public void friendsHandler(View v){
        //TODO: Send to friends page
    }
}
