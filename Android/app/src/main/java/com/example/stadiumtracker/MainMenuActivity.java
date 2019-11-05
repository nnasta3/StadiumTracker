package com.example.stadiumtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.stadiumtracker.data.User;

public class MainMenuActivity extends AppCompatActivity {
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        user = (User) savedInstanceState.getSerializable("user");
        Toast.makeText(getApplicationContext(),"id: "+user.getUserID(),Toast.LENGTH_SHORT).show();
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
