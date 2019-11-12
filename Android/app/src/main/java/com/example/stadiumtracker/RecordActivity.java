package com.example.stadiumtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.Team;
import com.example.stadiumtracker.data.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    EditText dateBox, city, homeScore, roadScore;
    Spinner stadiumSpinner, leagueSpinner, homeTeamSpinner, roadTeamSpinner;

    List<String> leagueList;
    List<Stadium> stadiumList;
    List<Team> homeTeamList, roadTeamList, fullTeamList;
    Stadium selectedStadium;
    String selectedLeague;
    Team selectedHomeTeam, selectedRoadTeam;

    Calendar calendar;
    String dateString;

    private Object lock;

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        lock = new Object();

        toolbar = findViewById(R.id.record_toolbar);
        dateBox = findViewById(R.id.date_box);
        city = findViewById(R.id.record_city);
        homeTeamSpinner = findViewById(R.id.record_home_team_spinner);
        homeScore = findViewById(R.id.record_home_score);
        roadTeamSpinner = findViewById(R.id.record_road_team_spinner);
        roadScore = findViewById(R.id.record_road_score);
        stadiumSpinner = findViewById(R.id.record_stadium_spinner);
        leagueSpinner = findViewById(R.id.record_league_spinner);
        selectedStadium = null;
        selectedLeague = null;
        selectedHomeTeam = null;
        selectedRoadTeam = null;

        user = (User) getIntent().getSerializableExtra("user");
        city.setFocusable(false);
        dateBox.setFocusable(false);

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
                    //add city and country to city field
                    String temp = selectedStadium.getCity()+", "+selectedStadium.getCountry();
                    city.setText(temp);
                    //Reorder home team spinner so that teams with this stadium as home stadium appear at the top
                    int j=1;
                    for(int i=1; i<homeTeamList.size(); i++){
                        if(selectedStadium.getStadiumID() == homeTeamList.get(i).getStadium().getStadiumID()){
                            Collections.swap(homeTeamList,i,j);
                            j++;
                        }
                    }
                    //TODO: Look to query API here
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //League Spinner list, start with hint "league"
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
                    //filter team spinners
                    List<Team> filteredTeams = filterTeams();

                    homeTeamSpinner.setSelection(0);
                    homeTeamList.clear();
                    homeTeamList.add(new Team(true));
                    homeTeamList.addAll(filteredTeams);

                    roadTeamSpinner.setSelection(0);
                    roadTeamList.clear();
                    roadTeamList.add(new Team(false));
                    roadTeamList.addAll(filteredTeams);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //try and catch statement for getting teams
        try{
            fullTeamList = new getTeams().execute().get();
        }catch (Exception e){
            Log.w("Error getTeams","" + e);
        }

        //home team spinner
        homeTeamList = new ArrayList<>();
        homeTeamList.add(new Team(true));
        homeTeamList.addAll(fullTeamList);

        ArrayAdapter homeTeamArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item,homeTeamList){
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
        homeTeamArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        homeTeamSpinner.setAdapter(homeTeamArrayAdapter);

        homeTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    selectedHomeTeam = homeTeamList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //road team spinner
        roadTeamList = new ArrayList<>();
        roadTeamList.add(new Team(false));
        roadTeamList.addAll(fullTeamList);

        ArrayAdapter roadTeamArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item,roadTeamList){
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
        roadTeamArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        roadTeamSpinner.setAdapter(roadTeamArrayAdapter);

        roadTeamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    selectedRoadTeam = roadTeamList.get(position);
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

    public List<Team> filterTeams(){
        List<Team> out = new ArrayList<>();
        for(int i=0; i<fullTeamList.size(); i++){
            if (fullTeamList.get(i).getLeague().equals(selectedLeague)){
                out.add(fullTeamList.get(i));
            }
        }
        return out;
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
        if(!validateFields()){
            //Fields are invalid in this condition, do nothing
            return;
        }
        String dateString = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        Event event;
        try{
            event = new eventsQuery().execute(String.valueOf(selectedStadium.getStadiumID()),dateString,String.valueOf(selectedHomeTeam.getTeamID()),String.valueOf(selectedRoadTeam.getTeamID()),homeScore.getText().toString(),roadScore.getText().toString(),selectedLeague).get();
        }catch (Exception e){
            event = null;
        }
        int eventID = -1;
        if(event == null){
            //create event
            try{
                eventID = new eventCreate().execute(String.valueOf(selectedStadium.getStadiumID()),dateString,String.valueOf(selectedHomeTeam.getTeamID()),String.valueOf(selectedRoadTeam.getTeamID()),homeScore.getText().toString(),roadScore.getText().toString(),selectedLeague).get();
            }catch (Exception e){
                Log.w("Error","" + e);
                Toast.makeText(getApplicationContext(),"Failed to record event",Toast.LENGTH_SHORT).show();
                return;
            }

        }else{
            eventID = event.getEventID();
        }
        //add visit to database
        try{
            if(!new visitCreate().execute(String.valueOf(eventID),String.valueOf(user.getUserID())).get()){
                Log.w("Error","visit create failed");
                Toast.makeText(getApplicationContext(),"Failed to record event",Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (Exception e){
            Log.w("Error","" + e);
            Toast.makeText(getApplicationContext(),"Failed to record event",Toast.LENGTH_SHORT).show();
            return;
        }
        //share popup and intent to main menu
        new AlertDialog.Builder(this)
                .setTitle("Share?")
                .setMessage("Would you like to share this visit?")
                .setIcon(R.drawable.share_icon)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String shareString = user.getName() + " visited " + selectedStadium.getName() + " on " + dateBox.getText().toString()
                                + " for the game between the " + selectedHomeTeam + " and the " + selectedRoadTeam;
                        //intent to main menu
                        Intent intent = new Intent(RecordActivity.this,MainMenuActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("shareString",shareString);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(RecordActivity.this,MainMenuActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                    }})
                .show();

    }

    public boolean validateFields(){
        //No validation needed for date field, user is not allowed to enter text into that field and the datepicker validates otherwise
        //Only need to check that the selected stadium is not null or not the hint field
        if(selectedStadium == null || stadiumSpinner.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(),"Please select a stadium",Toast.LENGTH_SHORT).show();
            return false;
        }
        //validate home team
        if(selectedHomeTeam == null || homeTeamSpinner.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(),"Please select a home team",Toast.LENGTH_SHORT).show();
            return false;
        }
        //validate road team
        if(selectedRoadTeam == null || roadTeamSpinner.getSelectedItemPosition()==0){
            Toast.makeText(getApplicationContext(),"Please select a road team",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Make sure home team and road team are different
        if(selectedHomeTeam.getTeamID() == selectedRoadTeam.getTeamID()){
            Toast.makeText(getApplicationContext(),"Selected teams cannot be the same",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Make sure home team and road team are from same league
        if(!selectedHomeTeam.getLeague().equals(selectedRoadTeam.getLeague())){
            Toast.makeText(getApplicationContext(),"Selected teams must be in the same league",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Validate home score
        String homescoreString = homeScore.getText().toString();
        try{
            int homeScoreInt = Integer.parseInt(homescoreString);
            if(homeScoreInt < 0){
                Toast.makeText(getApplicationContext(),"Home score must be a positive integer",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Home score must be a number format",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Validate road score
        String roadscoreString = roadScore.getText().toString();
        try{
            int roadScoreInt = Integer.parseInt(roadscoreString);
            if(roadScoreInt < 0){
                Toast.makeText(getApplicationContext(),"Road score must be a positive integer",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Road score must be a number format",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Validate league
        if(selectedLeague == null){
            Toast.makeText(getApplicationContext(),"Please select a league",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            calendar.set(year,month,dayOfMonth);
            //TODO: Look to query api here if selected stadium is not null
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
                ResultSet rs = stmt.executeQuery("Select StadiumID,Name,City,Country,Gps_Lat,Gps_Long from [Stadium] Order By Name");
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
                Log.w("Error all stadiums", "" + e);
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
                ResultSet rs = stmt.executeQuery("Select Abbrev from [League] Order By Abbrev");
                while(rs.next()){
                    String name = rs.getString(1);
                    out.add(name);
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error all leagues", "" + e);
                return out;
            }
            return out;
        }
    }

    class  eventsQuery extends AsyncTask<String, Void, Event> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Event doInBackground(String... strings) {
            //Strings[0] = stadiumID
            //Strings[1] = date
            //Strings[2] = home team
            //Strings[3] = road team
            //Strings[4] = home score
            //Strings[5] = road score
            //Strings[6] = league
            Event out;
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select * from [Event] where StadiumID="+strings[0]+" AND Date='"+strings[1]+"' AND Home_TeamID="+strings[2]+" AND Road_TeamID="+strings[3]+" AND Home_Score="+strings[4]+" AND Away_Score="+strings[5]+" AND League='"+strings[6]+"'");
                if(rs.next()){
                    int eventID = rs.getInt(1);
                    int stadiumID = rs.getInt(2);
                    Date date = rs.getDate(3);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String homeTeam = rs.getString(4);
                    String roadTeam = rs.getString(5);
                    int homeScore = rs.getInt(6);
                    int roadScore = rs.getInt(7);
                    String league = rs.getString(8);
                    out = new Event(eventID,stadiumID, cal, homeTeam,roadTeam,homeScore,roadScore,league);
                }else{
                    return null;
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error events query", "" + e);
                return null;
            }
            return out;
        }
    }

    class  eventCreate extends AsyncTask<String, Void, Integer> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Integer doInBackground(String... strings) {
            //Strings[0] = stadiumID
            //Strings[1] = date
            //Strings[2] = home team
            //Strings[3] = road team
            //Strings[4] = home score
            //Strings[5] = road score
            //Strings[6] = league
            int out = -1;
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [Event](StadiumID,Date,Home_TeamID,Road_TeamID,Home_Score,Away_Score,League) VALUES (convert(int,?),convert(date,?),convert(int,?),convert(int,?),convert(int,?),convert(int,?),?);");
                ps.setString(1,strings[0]);
                ps.setString(2,strings[1]);
                ps.setString(3,strings[2]);
                ps.setString(4,strings[3]);
                ps.setString(5,strings[4]);
                ps.setString(6,strings[5]);
                ps.setString(7,strings[6]);
                ps.executeUpdate();

                //Query the database for the eventID to return
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select EventID from [Event] where StadiumID="+strings[0]+" AND Date='"+strings[1]+"' AND Home_TeamID="+strings[2]+" AND Road_TeamID="+strings[3]+" AND Home_Score="+strings[4]+" AND Away_Score="+strings[5]+" AND League='"+strings[6]+"'");
                rs.next();
                out = rs.getInt(1);
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error event create", "" + e);
                return out;
            }
            return out;
        }
    }

    class  visitCreate extends AsyncTask<String, Void, Boolean> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected Boolean doInBackground(String... strings) {
            //Strings[0] = eventID
            //Strings[1] = visitID
            int out = -1;
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                PreparedStatement ps = DbConn.prepareStatement("INSERT INTO [Visit](EventID,UserID) VALUES (convert(int,?),convert(int,?));");
                ps.setString(1,strings[0]);
                ps.setString(2,strings[1]);
                ps.executeUpdate();
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error visit create", "" + e);
                return false;
            }
            return true;
        }
    }

    class  getTeams extends AsyncTask<String, Void, List<Team>> {
        String ip = getResources().getString(R.string.ip);
        String port = getResources().getString(R.string.port);
        String dbName = getResources().getString(R.string.db_name);
        String user = getResources().getString(R.string.masterUser);
        String pass = getResources().getString(R.string.masterPass);
        @Override
        protected List<Team> doInBackground(String... strings) {
            List<Team> out = new ArrayList<>();
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + user + ";password=" + pass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select * from [Team] Order By City,Nickname");
                while(rs.next()){
                    int id = rs.getInt(1);
                    String city = rs.getString(2);
                    String nickname = rs.getString(3);
                    String abbrev = rs.getString(4);
                    int stadiumID = rs.getInt(5);
                    Stadium stadium = getStadiumById(stadiumID);
                    String league = rs.getString(6);
                    out.add(new Team(id,city,nickname,abbrev,stadium,league));
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error visit create", "" + e);
                return out;
            }
            return out;
        }
    }

    public Stadium getStadiumById(int id){
        for(int i=0; i<stadiumList.size(); i++){
            if (stadiumList.get(i).getStadiumID() == id){
                return stadiumList.get(i);
            }
        }
        return null;
    }
}