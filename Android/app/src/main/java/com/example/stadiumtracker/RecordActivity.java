package com.example.stadiumtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    Stadium selectedStadium;

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
        selectedStadium = null;

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        dateString = calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        dateBox.setText(dateString);

        // Stadium Spinner list, start with hint "stadium"
        stadiumList = new ArrayList<>();
        stadiumList.add(new Stadium());
        addStadiums();

        stadiumList.add(new Stadium(0,"example","example","example"));
        //TODO: Stadium spinner setup
        ArrayAdapter<Stadium> stadiumArrayAdapter = new ArrayAdapter<Stadium>(this, R.layout.spinner_item,stadiumList){
          @Override
          public boolean isEnabled(int position){
              if(position == 0){
                  return false;
              }else{
                  return true;
              }
          }
          @Override
          public  View getDropDownView(int position, View convertView, ViewGroup parent){
              View view = super.getDropDownView(position, convertView, parent);
              TextView textView = (TextView) view;
              if(position == 0){
                   textView.setTextColor(Color.GRAY);
              }else{
                  textView.setTextColor(Color.BLACK);
              }
              return view;
          }
        };
        stadiumArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        stadiumSpinner.setAdapter(stadiumArrayAdapter);

        stadiumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    selectedStadium = stadiumList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //TODO: League Spinner list, start with hint "league"
        leagueList = new ArrayList<>();
        leagueList.add("League");
        addLeagues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_gps:
                //TODO: perform gps search for closest stadium
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
            dateString = dayOfMonth+"/"+(month+1)+"/"+year;
            dateBox.setText(dateString);
        }
    };
}
