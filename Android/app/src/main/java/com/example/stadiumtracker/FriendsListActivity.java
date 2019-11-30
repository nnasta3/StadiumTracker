package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.WrapperListAdapter;

import com.example.stadiumtracker.data.Friend;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.friendsList;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendsListActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    ListView listView;
    List<Map<String, Date>> friends;
    //List<Map<String, Date>> convertedFriends;


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

        //Convert friends list to String
        ArrayList<String> convertedFriends = new ArrayList<String>();
        for(int i =0;i<friends.size();i++){
            convertedFriends.add(convertMapToString(friends.get(i)));
        }

        //Populate listView
        ArrayAdapter<String>friendAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, convertedFriends);
        listView.setAdapter(friendAdapter);


    }


    /*
        Converts mapped (friendName, Date) to string for display purposes
    */
    public String convertMapToString(Map<String,Date> map){
        StringBuilder retString = new StringBuilder();
        for(String key: map.keySet()){
            retString.append(key + "\nDate Added: "+ map.get(key));
        }
        return retString.toString();
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
                Intent intent2 = new Intent(this, MainMenuActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                return true;
        }
    }
}
