package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.deleteVisit;

public class VisitViewActivity extends AppCompatActivity {
    User user;
    Event event;
    Toolbar toolbar;
    TextView leagueText, homeTeamText, roadTeamText, scoresText, stadiumText, cityText, dateText;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_view);

        toolbar = findViewById(R.id.visit_view_toolbar);
        leagueText = findViewById(R.id.visit_view_league);
        homeTeamText = findViewById(R.id.visit_view_home_team);
        roadTeamText = findViewById(R.id.visit_view_road_team);
        scoresText = findViewById(R.id.visit_view_scores);
        stadiumText = findViewById(R.id.visit_view_stadium);
        cityText = findViewById(R.id.visit_view_city);
        dateText = findViewById(R.id.visit_view_date);

        user = (User) getIntent().getSerializableExtra("user");
        event = (Event) getIntent().getSerializableExtra("event");
        from = getIntent().getStringExtra("from");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //init values for text fields
        leagueText.setText(event.getLeague());
        homeTeamText.setText(event.getHomeTeam().toString());
        roadTeamText.setText(event.getRoadTeam().toString());
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
                String shareString = user.getName()+" attended the game between the "+event.getHomeTeam()+" and the "+event.getRoadTeam()+" at "+event.getStadium().getName()+" on "+event.getDateFullString()+". ";
                if (event.getHomeScore() > event.getRoadScore()){
                    shareString += "The "+event.getHomeTeam().getNickname()+" won the game by a score of "+event.getHomeScore()+" - "+event.getRoadScore();
                }else if (event.getRoadScore() > event.getHomeScore()){
                    shareString += "The "+event.getRoadTeam().getNickname()+" won the game by a score of "+event.getRoadScore()+" - "+event.getHomeScore();
                }else{
                    shareString += "The game resulted in a tie with a score of "+event.getHomeScore();
                }
                //Can say events[0] is most recent because of sql query
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.action_delete:
                //Delete visit from database
                new deleteVisit(this).execute(user.getUserID(),event.getEventID());
                //Send user back from whence they came
                if (from.equals("stadiumView")){
                    Intent backIntent = new Intent(this,StadiumViewActivity.class);
                    backIntent.putExtra("user", user);
                    backIntent.putExtra("stadium",event.getStadium());
                    backIntent.putExtra("numVisits",getIntent().getIntExtra("numVisits",0)-1);
                    backIntent.putExtra("from","stadiumList");
                    startActivity(backIntent);
                }else{
                    Intent backIntent = new Intent(this,VisitListActivity.class);
                    backIntent.putExtra("user", user);
                    startActivity(backIntent);
                }
                return true;
            default:
                //Back button pressed
                if (from.equals("stadiumView")){
                    Intent backIntent = new Intent(this,StadiumViewActivity.class);
                    backIntent.putExtra("user", user);
                    backIntent.putExtra("stadium",event.getStadium());
                    backIntent.putExtra("numVisits",getIntent().getIntExtra("numVisits",0));
                    backIntent.putExtra("from","stadiumList");
                    startActivity(backIntent);
                }else{
                    Intent backIntent = new Intent(this,VisitListActivity.class);
                    backIntent.putExtra("user", user);
                    startActivity(backIntent);
                }
                return true;

        }
    }

    public void toStadium(View v){
        Intent intent = new Intent(this,StadiumViewActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("stadium",event.getStadium());
        intent.putExtra("numVisits",-1);
        intent.putExtra("event",event);
        intent.putExtra("from","visitView");
        startActivity(intent);
    }
}
