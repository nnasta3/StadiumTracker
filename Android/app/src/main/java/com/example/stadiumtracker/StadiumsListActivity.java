package com.example.stadiumtracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.stadiumtracker.data.User;

import java.util.ArrayList;
import java.util.List;

public class StadiumsListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;

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
