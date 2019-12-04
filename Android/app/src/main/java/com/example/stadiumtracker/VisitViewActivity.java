package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;

public class VisitViewActivity extends AppCompatActivity {
    User user;
    Event event;
    Toolbar toolbar;
    TextView leagueText, teamsText, scoresText, stadiumText, cityText, dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_view);

        toolbar = findViewById(R.id.visit_view_toolbar);
        leagueText = findViewById(R.id.visit_view_league);
        teamsText = findViewById(R.id.visit_view_teams);
        scoresText = findViewById(R.id.visit_view_scores);
        stadiumText = findViewById(R.id.visit_view_stadium);
        cityText = findViewById(R.id.visit_view_city);
        dateText = findViewById(R.id.visit_view_date);

        user = (User) getIntent().getSerializableExtra("user");
        event = (Event) getIntent().getSerializableExtra("event");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //TODO: init values for text fields
        leagueText.setText(event.getLeague());
        String teams = event.getHomeTeam()+" vs. "+event.getRoadTeam();
        teamsText.setText(teams);
        String scores = event.getHomeScore()+" - "+event.getRoadScore();
        scoresText.setText(scores);
        stadiumText.setText(event.getStadium().toString());
        String city = event.getStadium().getCity()+", "+event.getStadium().getCountry();
        cityText.setText(city);
        dateText.setText(event.getDateFullString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_view_menu, menu);
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
                //Share a string containing username, stadium, number of visits, and most recent visit
                //TODO
                return true;
            default:
                //Back button pressed
                Intent backIntent = new Intent(this,VisitListActivity.class);
                backIntent.putExtra("user", user);
                startActivity(backIntent);
                return true;

        }
    }
}
