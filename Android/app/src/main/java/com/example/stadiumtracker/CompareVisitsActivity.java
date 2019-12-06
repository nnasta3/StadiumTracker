package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.stadiumtracker.data.User;

public class CompareVisitsActivity extends AppCompatActivity {

    User user;
    int userID;
    Toolbar toolbar;
    String friendName;
    int friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_visits);

        toolbar = findViewById(R.id.compare_visits_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
        friendName = (String) getIntent().getSerializableExtra("friendName");
        friendID = (int) getIntent().getSerializableExtra("friendID");
        userID = (int) getIntent().getSerializableExtra("userID");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compare_visits_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                Intent intent2 = new Intent(this, FriendViewActivity.class);
                intent2.putExtra("user", user);
                intent2.putExtra("userId",user.getUserID());
                intent2.putExtra("friendName",friendName);
                intent2.putExtra("friendID",friendID);
                startActivity(intent2);
                return true;
        }
    }
}
