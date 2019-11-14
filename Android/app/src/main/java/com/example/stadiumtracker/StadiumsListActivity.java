package com.example.stadiumtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.stadiumsCountForUser;
import com.example.stadiumtracker.helpers.StadiumListAdapter;
import com.example.stadiumtracker.helpers.StadiumListHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StadiumsListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;
    List<Stadium> stadiums;
    List<StadiumListHelper> stadiumListHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadiums_list);

        toolbar = findViewById(R.id.stadiums_list_toolbar);
        listView = findViewById(R.id.stadiums_list_list_view);

        user = (User) getIntent().getSerializableExtra("user");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        //initialize information in the listView
        try{
            stadiums = new allStadiums(this).execute().get();
        }catch (Exception e){
            Log.w("error allStadiums",e.toString());
        }
        //number of visits to each stadium for this user (defaulting to zero)
        Map<Integer, Integer> counts = new HashMap<>();
        try{
            counts = new stadiumsCountForUser(this).execute(user.getUserID()).get();
        }catch (Exception e){
            Log.w("error stadiumsCount", e.toString());
        }
        //create list of helper class
        stadiumListHelpers = new ArrayList<>();
        for (int i=0; i<stadiums.size(); i++){
            //enter the counts
            if (counts.containsKey(stadiums.get(i).getStadiumID())){
                stadiumListHelpers.add(new StadiumListHelper(stadiums.get(i),user,counts.get(stadiums.get(i).getStadiumID())));
            }else {
                stadiumListHelpers.add(new StadiumListHelper(stadiums.get(i),user,0));
            }
        }
        //adapt list of helper class to listView
        StadiumListAdapter stadiumListAdapter = new StadiumListAdapter(this,stadiumListHelpers,user);
        listView.setAdapter(stadiumListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stadiums_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                //TODO: share popup
                return true;
            case R.id.action_filter:
                //TODO: filter popup
                return true;
            case R.id.action_search:
                //TODO: search popup
                return true;
            case R.id.action_sort:
                //TODO: sort popup
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
