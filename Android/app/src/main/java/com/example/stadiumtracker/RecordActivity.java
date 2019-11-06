package com.example.stadiumtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    EditText dateBox, city, homeTeam, roadTeam, homeScore, roadScore;
    Spinner stadiumSpinner, leagueSpinner;

    List<String> leagueList;
    List<Stadium> stadiumList;

    Calendar calendar;
    String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        toolbar = findViewById(R.id.record_toolbar);
        dateBox = findViewById(R.id.date_box);
        city = findViewById(R.id.record_city);
        homeTeam = findViewById(R.id.record_home_team);
        homeScore = findViewById(R.id.record_home_score);
        roadTeam = findViewById(R.id.record_road_team);
        roadScore = findViewById(R.id.record_road_score);
        stadiumSpinner = findViewById(R.id.record_stadium_spinner);
        leagueSpinner = findViewById(R.id.record_league_spinner);

        calendar = Calendar.getInstance();
        dateString = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);
        dateBox.setText(dateString);

        //TODO: Stadium Spinner list, start with hint "stadium"
        stadiumList = new ArrayList<>();
        //stadiumList.add("Stadium");
        addStadiums();

        //TODO: League Spinner list, start with hint "league"
        leagueList = new ArrayList<>();
        leagueList.add("League");
        addLeagues();
    }

    private void addStadiums(){
        //TODO: query database for all stadiums

        //TODO: add all stadiums to stadiumList
    }

    private void addLeagues(){
        //TODO: query database for all Leagues

        //TODO: add all stadiums to leagueList
    }

    public void recordHandler(View v){
        /*
            TODO:
                Check if event exists in database
                    if not, add it
                Record visit in database
        */
    }

    public void datePickerHandler(View v){
        showDialog(10);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == 10){
            return new DatePickerDialog(this, dateListener, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateString = dayOfMonth+"/"+month+"/"+year;
            dateBox.setText(dateString);
        }
    };
}
