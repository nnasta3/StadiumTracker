package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.selectDistinctStadiumsVisited;
import com.example.stadiumtracker.helpers.CompareStadiumsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompareStadiumsActivity extends AppCompatActivity {

    User user;
    int userID;
    Toolbar toolbar;
    String friendName;
    int friendID;
    ListView listView;
    List<Stadium> stadiums;
    TextView usernameTextView;
    TextView friendNameTextView;
    ArrayList<Integer> userIDs;
    ArrayList<Integer> friendStadiumIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_stadiums);

        toolbar = findViewById(R.id.compare_stadiums_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
        friendName = (String) getIntent().getSerializableExtra("friendName");
        friendID = (int) getIntent().getSerializableExtra("friendID");
        userID = (int) getIntent().getSerializableExtra("userID");

        usernameTextView = findViewById(R.id.username_text_view);
        usernameTextView.setText(user.getName());
        friendNameTextView = findViewById(R.id.friend_name_text_view);
        friendNameTextView.setText(friendName);

        listView = findViewById(R.id.compare_stadiums_list_view);
        try{
            stadiums = new allStadiums(this).execute().get();
            userIDs = new selectDistinctStadiumsVisited(this).execute(user.getUserID()).get();
            friendStadiumIDs = new selectDistinctStadiumsVisited(this).execute(friendID).get();
        } catch (Exception e){
            Log.w("error allStadiums",e.toString());
        }
        //Pass the List/Mapping of Stadium,State,Country
        CompareStadiumsListAdapter adapter = new CompareStadiumsListAdapter(stadiums,this,userIDs,friendStadiumIDs);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compare_stadiums_menu, menu);
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
