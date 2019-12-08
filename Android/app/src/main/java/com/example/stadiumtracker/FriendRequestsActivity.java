package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.helpers.FriendRequestListAdapter;
import java.util.ArrayList;

public class FriendRequestsActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    ListView listView;
    ArrayList<String> friendRequests = new ArrayList<String>();

    /* NICHOLAS NASTA
     * Create the FriendRequestsActivity
     * Use findViewByID to get ui components
     * Pulls the list of friend requests for the current user from the FriendsListActivity Intent
     * Populates the ListView with the names of the friends and 2 buttons to add or delete
     * If a user selects add the friend is added to the database and the request is deleted
     * If a user selects delete the friend request is deleted
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        toolbar = findViewById(R.id.friend_requests_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");

        listView = findViewById(R.id.friends_request_list);
        friendRequests = getIntent().getStringArrayListExtra("friendRequests");

        FriendRequestListAdapter adapter = new FriendRequestListAdapter(friendRequests,this,user);
        listView.setAdapter(adapter);
    }

    /* NICHOLAS NASTA
     * Creates the menu for the FriendRequestActivity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_requests_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* NICHOLAS NASTA
     * Handles when a user presses the back button and the logout button
     * The back button sends the user to their Friends List
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                Intent intent2 = new Intent(this, FriendsListActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                return true;
        }
    }
}
