package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import com.example.stadiumtracker.data.Friend;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.friendsList;

import java.util.ArrayList;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    ListView listView;
    List<Friend> friends;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        toolbar = findViewById(R.id.friends_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);


        user = (User) getIntent().getSerializableExtra("user");

        listView = findViewById(R.id.friends_list_list_view);

        //Build Friends List
        try{
            friends = new friendsList(this).execute(user.getUserID()).get();

        }catch (Exception e){
            Log.w("error friendsList",e.toString());
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_friend://This needs to be add a friend
                //TODO: handle add a friend
                return true;
            case R.id.action_friend_requests:
                //TODO: handle friend requests
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
