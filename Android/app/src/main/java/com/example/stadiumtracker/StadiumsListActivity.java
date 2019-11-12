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
import android.widget.ListView;
import android.widget.Toast;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.helpers.StadiumListAdapter;
import com.example.stadiumtracker.helpers.StadiumListHelper;

import java.util.ArrayList;
import java.util.List;

public class StadiumsListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;
    List<Stadium> stadiums;
    List<StadiumListHelper> stadiumListHelpers;
    List<Integer> visitCounts;

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

        //TODO: initialize information in the listView

        /*
            TODO: info needed for list
                1. all stadiums, sorted by name
                2. number of visits to each stadium for this user
                A helper class containing a stadium, user, and int would probably be best for the listView
                Selecting an item on the list can bring up the stadiumView for that particular stadium
         */
        try{
            stadiums = new allStadiums(this).execute().get();
        }catch (Exception e){
            Log.w("error allStadiums",e.toString());
        }
        //TODO: number of visits to each stadium for this user (defaulting to zero for now)
        //visitCounts = some new query class
        //TODO: create list of helper class
        stadiumListHelpers = new ArrayList<>();
        for (int i=0; i<stadiums.size(); i++){
            //TODO: replace this 0 with reference to visitCounts
            stadiumListHelpers.add(new StadiumListHelper(stadiums.get(i),user,0));
        }
        //TODO: adapt list of helper class to listView
        StadiumListAdapter stadiumListAdapter = new StadiumListAdapter(this,stadiumListHelpers);
        listView.setAdapter(stadiumListAdapter);
        listView.setOnItemClickListener((p,v,pos,id)-> stadiumHandler(pos));
        listView.setScrollContainer(true);
        //TODO: figure out why this doesnt add a divider
        int[] dividerColor = {0,0xFF0000,0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT,dividerColor));
        listView.setDividerHeight(2);
    }

    private void stadiumHandler(int pos){
        Toast.makeText(this,"pos = "+pos,Toast.LENGTH_SHORT).show();
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
    /*TODO:
        figure out listview
     */
}
