package com.example.stadiumtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.getFriendViewDetails;
import com.example.stadiumtracker.database.removeFriend;

import java.util.ArrayList;

public class FriendViewActivity extends AppCompatActivity {

    User user;
    Toolbar toolbar;
    TextView friendNameTextView;
    String friendName;
    int friendID;
    TextView stadiumsCount;
    TextView visitsCount;

    /*NICHOLAS NASTA
     *Creates the FriendViewActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_view);

        toolbar = findViewById(R.id.friend_view_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        stadiumsCount = findViewById(R.id.stadiums_visited_count);
        visitsCount = findViewById(R.id.visits_count);

        user = (User) getIntent().getSerializableExtra("user");
        friendName = (String) getIntent().getSerializableExtra("friendName");

        friendNameTextView = findViewById(R.id.FriendName);
        friendNameTextView.setText(friendName);
        friendNameTextView.setTextColor(0xff669900);
        friendNameTextView.setTextSize(32);

        friendID = (int) getIntent().getSerializableExtra("friendID");

        getCounts();

    }

    /*NICHOLAS NASTA
     *Returns the number of stadiums visited and events visited for the currently selected friend
     */
    public void getCounts(){
        ArrayList<Integer> getCounts= new ArrayList<Integer>();
        try {
            getCounts = new getFriendViewDetails(this).execute(friendID).get();
            stadiumsCount.setText(getCounts.get(0).toString());
            visitsCount.setText(getCounts.get(1).toString());
        } catch (Exception e){
            Log.w("error getFriendViewDetails",e.toString());
        }
    }

    /*NICHOLAS NASTA
     *Sends the user to the CompareStadiumsActivity
     */
    public void compareStadiums(View v){
        Intent intent = new Intent(this, CompareStadiumsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userID",user.getUserID());
        intent.putExtra("friendName",friendName);
        intent.putExtra("friendID",friendID);
        startActivity(intent);
    }

    /*NICHOLAS NASTA
     *Sends the user to the CompareVisitsActivity
     */
    public void compareVisits(View v){
        Intent intent = new Intent(this, CompareVisitsActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("userID",user.getUserID());
        intent.putExtra("friendName",friendName);
        intent.putExtra("friendID",friendID);
        startActivity(intent);
    }

    /*NICHOLAS NASTA
     *Creates the menu from the friend menu layout
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*NICHOLAS NASTA
     *Handles the menu options for logout, remove friend, and go back options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_removeFriend:
                try{
                    new removeFriend(this).execute(friendID,user.getUserID());
                } catch (Exception e){
                    Log.w("error removeFriend",e.toString());
                }
                Toast.makeText(FriendViewActivity.this, "Sucessfully Removed "+friendName+" From Your Friends List", Toast.LENGTH_LONG).show();
                //Send the user back to their friends list
                Intent intent3 = new Intent(this, FriendsListActivity.class);
                intent3.putExtra("user", user);
                startActivity(intent3);
                return true;
            default:
                Intent intent2 = new Intent(this, FriendsListActivity.class);
                intent2.putExtra("user", user);
                startActivity(intent2);
                return true;
        }
    }
}
