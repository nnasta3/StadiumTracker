package com.example.stadiumtracker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.friendsList;
import com.example.stadiumtracker.database.addFriend;
import com.example.stadiumtracker.database.getFriendIDFromName;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendsListActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    ListView listView;
    List<String> friends;
    int addFriend = -2;
    Context context = this.getContext();

    public Context getContext() {
        return context;
    }
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

        //Populate listView
        ArrayAdapter<String>friendAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friends);
        listView.setAdapter(friendAdapter);

        //Present User with more detailed info about their friend (compare pages) and option to remove friend
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friendView(friends.get(position),position);
            }
        });

    }

    /*
        Go to friendView of selected friend
    */
    public void friendView(String friendName, int position){
        Intent intent = new Intent(this,FriendViewActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("friendName", friendName);
        try {
            int friendID = new getFriendIDFromName(this).execute(friends.get(position)).get();
            intent.putExtra("friendID", friendID);
        }
        catch (Exception e){
            Log.w("error getFriendIDFromName",e.toString());
        }

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog = new Dialog(this);
        Button add;
        Button cancel;
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_friend://Enter into DB and then update userXAccept in DB when user X accepts and update Date to current time
                //search popup
                dialog.setContentView(R.layout.add_friend_popup);
                dialog.setTitle("Add Friend");

                EditText editText = (EditText) dialog.findViewById(R.id.search_popup_input);
                add = (Button) dialog.findViewById(R.id.search_popup_confirm);
                cancel = (Button) dialog.findViewById(R.id.search_popup_cancel);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //call handler, then dismiss
                        String searchParam = editText.getText().toString();
                        int check = searchHandler(searchParam);
                        if (check ==-1){//User does not exist
                            Toast.makeText(FriendsListActivity.this, "That User Does Not Exist", Toast.LENGTH_LONG).show();
                        }
                        else if(check == 0){//Successfully created a friend request and is pending other user acceptance
                            Toast.makeText(FriendsListActivity.this, "A Friend Request Has Been Sent To " + searchParam, Toast.LENGTH_LONG).show();
                        }
                        else if(check == 1){//Friend request already exists
                            Toast.makeText(FriendsListActivity.this, "That User Is Already Your Friend, Or A Request Has Already Been Sent", Toast.LENGTH_LONG).show();
                        }
                        else if(check == -2){//Error in DB
                            Toast.makeText(FriendsListActivity.this, "Error Connecting to Database, Please Try Again", Toast.LENGTH_LONG).show();
                        }
                        else if(check == -3){
                            Toast.makeText(FriendsListActivity.this, "You Cannot Add Yourself As A Friend", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;

            case R.id.action_friend_requests://List of requests, when a user accepts save that in database
                //TODO: handle friend requests
                return true;

            default:
                Intent intent2 = new Intent(this, MainMenuActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                return true;
        }
    }
    public int searchHandler(String param){
        try{
            addFriend = new addFriend(this).execute(param,Integer.toString(user.getUserID())).get();
        }catch (Exception e){
            Log.w("error addFriend",e.toString());
        }
        return addFriend;
    }
}
