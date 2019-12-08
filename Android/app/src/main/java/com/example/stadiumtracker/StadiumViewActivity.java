package com.example.stadiumtracker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.VisitsForUserToStadium;
import com.example.stadiumtracker.helpers.StadiumViewAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/*
    TODO List for this file:
        1. Share button
        2. In StadiumViewAdapter: Onclick visit list item to visit view
 */

public class StadiumViewActivity extends AppCompatActivity {
    User user;
    Stadium stadium;
    int numVisits;
    Toolbar toolbar;
    ImageView imageView;
    TextView nameText, cityText, visitsText;
    ListView listView;
    List<Event> events;
    StadiumViewAdapter stadiumViewAdapter;
    String from;

    /*John Strauser
        Initializes UI
        gets UI components from findViewByID
        gets information from intent
        sets up toolbar
        Gets stadium image from URL
        Sets stadium information
        Gets visits to stadium for user from query
        sets listview
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_view);

        toolbar = (Toolbar) findViewById(R.id.stadium_view_toolbar);
        imageView = (ImageView) findViewById(R.id.stadium_view_image);
        nameText = (TextView) findViewById(R.id.stadium_view_name);
        cityText = (TextView) findViewById(R.id.stadium_view_city);
        visitsText = (TextView) findViewById(R.id.stadium_view_num_visits);
        listView = (ListView) findViewById(R.id.stadium_view_list_view);

        user = (User) getIntent().getSerializableExtra("user");
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        numVisits = (int) getIntent().getIntExtra("numVisits",0);
        from = getIntent().getStringExtra("from");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //init image
        try{
            Drawable drawable = new ImageFromURL().execute(stadium.getImageURL()).get();
            if (drawable != null){
                //Get from URL was successful, replace default image
                imageView.setImageDrawable(drawable);
            }else {
                Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("getImage create",e.toString());
        }

        //init text views
        nameText.setText(stadium.getName());
        String temp = stadium.getCity()+", "+stadium.getCountry();
        cityText.setText(temp);
        temp = numVisits+" visits";
        visitsText.setText(temp);

        //init list view
        try{
            events = new VisitsForUserToStadium(this,stadium).execute(user.getUserID(),stadium.getStadiumID()).get();
        }catch (Exception e){
            Log.e("stadViewListInit",e.toString());
            events = null;
        }

        if (events != null){
            if (events.size() > 0){
                stadiumViewAdapter = new StadiumViewAdapter(this,events,user,numVisits);
                listView.setAdapter(stadiumViewAdapter);
            }
            if (events.size() != numVisits){
                numVisits = events.size();
                temp = numVisits+" visits";
                visitsText.setText(temp);
            }
        }
    }
    /*John Strauser
        Initialize toolbar UI
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stadium_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*John Strauser
        called when toolbar button is clicked
        action_logout:
            logout button
            sends user to login activity
        action_share:
            share button
            Generate string to share
            sends to android share sheet
        default:
            Back button
            sends user back to either stadium list or visit view depending on where they came from
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                //Share a string containing username, stadium, number of visits, and most recent visit
                String shareString = user.getName()+" has visited "+stadium.getName()+" "+numVisits;
                if (numVisits == 1){
                    shareString += " time ";
                }else {
                    shareString += " times ";
                }
                shareString += "with the most recent being on "+events.get(0).getDateFullString();
                //Can say events[0] is most recent because of sql query
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            default:
                //Back button pressed
                if (from.equals("visitView")){
                    Intent backIntent = new Intent(this,VisitViewActivity.class);
                    backIntent.putExtra("user", user);
                    backIntent.putExtra("event",getIntent().getSerializableExtra("event"));
                    backIntent.putExtra("from","visitList");
                    startActivity(backIntent);
                }else {
                    Intent backIntent = new Intent(this,StadiumsListActivity.class);
                    backIntent.putExtra("user", user);
                    startActivity(backIntent);
                }

                return true;

        }
    }
    /*John Strauser
        Gets a drawable from URL provided
        used for picture in stadium view activity
     */
    public class  ImageFromURL extends AsyncTask<String, Void, Drawable>{
        @Override
        protected Drawable doInBackground(String... strings) {
            try{
                InputStream inputStream = (InputStream) new URL(strings[0]).getContent();
                return Drawable.createFromStream(inputStream,"image");
            }catch (Exception e){
                Log.e("getImage",e.toString());
                return null;
            }
        }
    }
}
