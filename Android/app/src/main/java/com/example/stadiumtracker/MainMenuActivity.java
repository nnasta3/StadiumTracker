package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;

import com.example.stadiumtracker.data.User;

public class MainMenuActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    String shareString;
    /*John Strauser
        initialize UI
        setup toolbar
        get information from the intent
        If sharestring was passed, call android share sheet
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.main_menu_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
        try{
            shareString = getIntent().getStringExtra("shareString");
            if (shareString != null){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        }catch (Exception e){
            Log.w("error sharing",e.toString());
        }
    }
    /*John Strauser
        initialize toolbar ui
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*John Strauser
        called when a toolbar button is clicked
        action_logout:
            logout button sends user to login activity
        default:
            back button sends user to login activity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    /*John Strauser
        sends user to record activity
     */
    public void recordHandler(View v){
        Intent intent = new Intent(this,RecordActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    /*John Strauser
        sends user to stadium list
     */
    public void stadiumHandler(View v){
        Intent intent = new Intent(this,StadiumsListActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    /*John Strauser
        sends user to visit list
     */
    public void visitHandler(View v){
        Intent intent = new Intent(this,VisitListActivity.class);
        intent.putExtra("user", user);
        Log.e("main menu","starting visit list");
        startActivity(intent);
    }
    /*John Strauser
        sends user to friends list
     */
    public void friendsHandler(View v){
        Intent intent = new Intent(this, FriendsListActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
