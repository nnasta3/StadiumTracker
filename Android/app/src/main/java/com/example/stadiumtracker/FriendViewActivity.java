package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.stadiumtracker.data.User;

public class FriendViewActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    TextView friendNameTextView;
    String friendName;
    int friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        toolbar = findViewById(R.id.friend_view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
        friendName = (String) getIntent().getSerializableExtra("friendName");

        friendNameTextView = findViewById(R.id.FriendName);
        friendNameTextView.setText(friendName);
        friendNameTextView.setTextColor(0xff669900);
        friendNameTextView.setTextSize(32);

        friendID = (int) getIntent().getSerializableExtra("friendID");

    }

    public void compareStadiums(View v){
        Intent intent = new Intent(this, CompareStadiumsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userID",user.getUserID());
        intent.putExtra("friendName",friendName);
        intent.putExtra("friendID",friendID);
        startActivity(intent);
    }

    public void compareVisits(View v){
        Intent intent = new Intent(this, CompareVisitsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userID",user.getUserID());
        intent.putExtra("friendName",friendName);
        intent.putExtra("friendID",friendID);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_removeFriend://TODO ADD REMOVE FRIEND FUNCTIONALITY
                return true;
            default:
                Intent intent2 = new Intent(this, FriendsListActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                return true;
        }
    }
}
