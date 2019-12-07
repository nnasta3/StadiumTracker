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

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allEventsForUser;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.selectDistinctStadiumsVisited;
import com.example.stadiumtracker.helpers.CompareStadiumsListAdapter;
import com.example.stadiumtracker.helpers.CompareVisitsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompareVisitsActivity extends AppCompatActivity {

    User user;
    int userID;
    Toolbar toolbar;
    String friendName;
    int friendID;
    TextView usernameTextView;
    TextView friendNameTextView;
    ListView listView;
    List<Event> userEvents;
    List<Event> friendEvents;
    ArrayList<Event> allEvents;
    ArrayList<Integer> userEventIDs;
    ArrayList<Integer> friendEventIDs;

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

        usernameTextView = findViewById(R.id.username_text_view);
        usernameTextView.setText(user.getName());
        friendNameTextView = findViewById(R.id.friend_name_text_view);
        friendNameTextView.setText(friendName);

        listView = findViewById(R.id.compare_visits_list_view);
        try{
            allEvents = new ArrayList<>();
            userEventIDs = new ArrayList<>();
            friendEventIDs = new ArrayList<>();
            userEvents = new allEventsForUser(this).execute(userID).get();
            friendEvents = new allEventsForUser(this).execute(friendID).get();

            for(Event e : userEvents){
                allEvents.add(e);
                userEventIDs.add(e.getEventID());
            }
            for(Event e : friendEvents){
                if(!allEvents.contains(e.getEventID())) {
                    allEvents.add(e);
                }
                friendEventIDs.add(e.getEventID());
            }

            CompareVisitsListAdapter adapter = new CompareVisitsListAdapter(allEvents,this,userEventIDs,friendEventIDs);
            listView.setAdapter(adapter);
        } catch (Exception e){
            Log.w("error compareEventsVisited",e.toString());
        }

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
