package com.example.stadiumtracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
    String selectedLeague;

    Calendar calendar;
    String dateString;

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

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

        ArrayAdapter stadiumArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item,stadiumList){
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
                if(position > 0){
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

        ArrayAdapter leagueArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item,leagueList){
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
        leagueArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        leagueSpinner.setAdapter(leagueArrayAdapter);

        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    selectedLeague = leagueList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                //Get gps coords of device
                if(!(PackageManager.PERMISSION_GRANTED==checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION))){
                    requestPermissions(LOCATION_PERMS,LOCATION_REQUEST);
                }
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                try{
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //Calculate distance from each stadium in stadiumList
                    List<Double> dists = new ArrayList<>();
                    for(int i=0; i<stadiumList.size(); i++){
                        double x = location.getLatitude() - stadiumList.get(i).getGpsLat();
                        double y = location.getLongitude() - stadiumList.get(i).getGpsLong();
                        double dist = Math.sqrt((x*x)+(y*y));
                        dists.add(dist);
                    }
                    //Pick smallest one and set selection to that one
                    int smallestIndex = getSmallest(dists);
                    stadiumSpinner.setSelection(smallestIndex);
                    selectedStadium = stadiumList.get(smallestIndex);
                }catch (SecurityException e){
                    Log.w("Error location","" + e);
                }catch (Exception e){
                    Log.w("Error location","" + e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private int getSmallest(List<Double> list){
        int out = 0;
        for(int i=1; i<list.size(); i++){
            if(list.get(out) > list.get(i)){
                out = i;
            }
        }
        return out;
    }
    private void addStadiums(){
        try{
            List<Stadium> all = new allStadiums().execute().get();
            stadiumList.addAll(all);
        }catch (Exception e){
            Log.w("Error","" + e);
        }
    }

    private void addLeagues(){
        try{
            List<String> all = new allLeagues().execute().get();
            leagueList.addAll(all);
        }catch (Exception e){
            Log.w("Error","" + e);
        }
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

    class  allStadiums extends AsyncTask<String, Void, List<Stadium>> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected List<Stadium> doInBackground(String... strings) {
            List<Stadium> out = new ArrayList<>();
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select StadiumID,Name,City,Country,Gps_Lat,Gps_Long from [Stadium]");
                while(rs.next()){
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    String city = rs.getString(3);
                    String country = rs.getString(4);
                    double gpsLat = rs.getDouble(5);
                    double gpsLong = rs.getDouble(6);
                    out.add(new Stadium(id,name,city,country,gpsLat,gpsLong));
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error connection", "" + e);
                return out;
            }
            return out;
        }
    }

    class  allLeagues extends AsyncTask<String, Void, List<String>> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected List<String> doInBackground(String... strings) {
            List<String> out = new ArrayList<>();
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select League from [League]");
                while(rs.next()){
                    String name = rs.getString(1);
                    out.add(name);
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error connection", "" + e);
                return out;
            }
            return out;
        }
    }
}