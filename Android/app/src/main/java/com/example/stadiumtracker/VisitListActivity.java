package com.example.stadiumtracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.Team;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allEventsForUser;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.allTeams;
import com.example.stadiumtracker.helpers.StadiumListAdapter;
import com.example.stadiumtracker.helpers.StadiumListHelper;
import com.example.stadiumtracker.helpers.VisitListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class VisitListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;
    List<Event> events, allEvents;
    VisitListAdapter visitListAdapter;
    /*John Strauser
        Initializes UI
        gets UI components from findViewByID
        gets information from intent
        sets up toolbar
        Gets list of events from query
        sets up listview
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);
        Log.e("visit list","oncreate started");
        toolbar = findViewById(R.id.visit_list_toolbar);
        listView = findViewById(R.id.visit_list_list_view);

        user = (User) getIntent().getSerializableExtra("user");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
        Log.e("visit list","init setup done");
        //Query for all events with this user
        try{
            events = new allEventsForUser(this).execute(user.getUserID()).get();
            allEvents = new ArrayList<>();
            allEvents.addAll(events);
            Log.e("visit list","query done. size of events="+events.size());
        }catch (Exception e){
            Log.e("query for events",e.toString());
            events = new ArrayList<>();
        }
        //Create adapter
        visitListAdapter = new VisitListAdapter(events,this,user);
        //Set adapter for listview
        listView.setAdapter(visitListAdapter);
    }
    /*John Strauser
        Initializes toolbar UI
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*John Strauser
        called when toolbar button is clicked
        action_logout:
            logout button
            sends user to login activity
        action_filter:
            filter button
            pops up dialog for filter and calls filter handler
        action_sort:
            sort button
            pops up dialog for sort and calls sort handler
        action_search:
            search button
            pops up dialog for search and call search handler
        action_refresh:
            refresh button
            resets listview to include all events
        default:
            back button
            sends user back to main menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dialog = new Dialog(this);
        Button confirm;
        Button cancel;
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_filter:
                //TODO: filter popup
                /*
                    popup with dropdowns for filters
                        -city
                        -country
                        -league

                 */
                dialog.setContentView(R.layout.visit_filter_popup);
                dialog.setTitle("Filter");

                Spinner teamSpinner = (Spinner) dialog.findViewById(R.id.visit_filter_team_spinner);
                Spinner stadiumSpinner = (Spinner) dialog.findViewById(R.id.visit_filter_stadium_spinner);
                Spinner leagueSpinner = (Spinner) dialog.findViewById(R.id.visit_filter_league_spinner);

                EditText startDateText = (EditText) dialog.findViewById(R.id.visit_filter_start_date_box);
                ImageButton startDateButton = (ImageButton) dialog.findViewById(R.id.visit_filter_start_date_button);
                Calendar startDate = Calendar.getInstance();

                EditText endDateText = (EditText) dialog.findViewById(R.id.visit_filter_end_date_box);
                ImageButton endDateButton = (ImageButton) dialog.findViewById(R.id.visit_filter_end_date_button);
                Calendar endDate = Calendar.getInstance();
                startDate.set(endDate.get(Calendar.YEAR),0,1);

                EditText scoreMaxText = (EditText) dialog.findViewById(R.id.visit_filter_max_text);
                RadioGroup scoreMaxRG = (RadioGroup) dialog.findViewById(R.id.visit_filter_max_radio_group);
                scoreMaxRG.check(R.id.visit_filter_max_radio_ind);

                EditText scoreMinText = (EditText) dialog.findViewById(R.id.visit_filter_min_text);
                RadioGroup scoreMinRG = (RadioGroup) dialog.findViewById(R.id.visit_filter_min_radio_group);
                scoreMinRG.check(R.id.visit_filter_min_radio_ind);

                confirm = (Button) dialog.findViewById(R.id.visit_filter_confirm);
                cancel = (Button) dialog.findViewById(R.id.visit_filter_cancel);

                List<String> teams = new ArrayList<>();
                List<String> stadiums = new ArrayList<>();
                List<String> leagues = new ArrayList<>();

                teams.add("Team");
                stadiums.add("Stadium");
                leagues.add("League");

                for (Event event : events){
                    String homeTeam = event.getHomeTeam().getNickname();
                    String roadTeam = event.getRoadTeam().getNickname();
                    if (!teams.contains(homeTeam)){
                        teams.add(homeTeam);
                    }
                    if (!teams.contains(roadTeam)){
                        teams.add(roadTeam);
                    }

                    String stadium = event.getStadium().getName();
                    if (!stadiums.contains(stadium)){
                        stadiums.add(stadium);
                    }

                    String league = event.getLeague();
                    if (!leagues.contains(league)){
                        leagues.add(league);
                    }
                }

                ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teams);
                ArrayAdapter<String> stadiumAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stadiums);
                ArrayAdapter<String> leagueAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, leagues);

                teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stadiumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                leagueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                teamSpinner.setAdapter(teamAdapter);
                stadiumSpinner.setAdapter(stadiumAdapter);
                leagueSpinner.setAdapter(leagueAdapter);

                String temp = (startDate.get(Calendar.MONTH)+1)+"/"+startDate.get(Calendar.DAY_OF_MONTH)+"/"+startDate.get(Calendar.YEAR);
                startDateText.setText(temp);
                temp = (endDate.get(Calendar.MONTH)+1)+"/"+endDate.get(Calendar.DAY_OF_MONTH)+"/"+endDate.get(Calendar.YEAR);
                endDateText.setText(temp);

                startDateButton.setOnClickListener(new View.OnClickListener() {
                    DatePickerDialog datePickerDialog;
                    @Override
                    public void onClick(View v) {
                        //display date picker dialog, then set the calendar and date
                        datePickerDialog = new DatePickerDialog(dialog.getContext(), dateListener, startDate.get(Calendar.YEAR),startDate.get(Calendar.MONTH),startDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }

                    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            startDate.set(year,month,dayOfMonth);
                            String temp = (startDate.get(Calendar.MONTH)+1)+"/"+startDate.get(Calendar.DAY_OF_MONTH)+"/"+startDate.get(Calendar.YEAR);
                            startDateText.setText(temp);
                            datePickerDialog.dismiss();
                        }
                    };
                });

                endDateButton.setOnClickListener(new View.OnClickListener() {
                    DatePickerDialog datePickerDialog;
                    @Override
                    public void onClick(View v) {
                        //display date picker dialog, then set the calendar and date
                        datePickerDialog = new DatePickerDialog(dialog.getContext(), dateListener, endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH),endDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }

                    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            endDate.set(year,month,dayOfMonth);
                            String temp = (endDate.get(Calendar.MONTH)+1)+"/"+endDate.get(Calendar.DAY_OF_MONTH)+"/"+endDate.get(Calendar.YEAR);
                            endDateText.setText(temp);
                            datePickerDialog.dismiss();
                        }
                    };
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //call handler
                        String team = (String) teamSpinner.getSelectedItem();
                        String stadium = (String) stadiumSpinner.getSelectedItem();
                        String league = (String) leagueSpinner.getSelectedItem();
                        //date range is startDate and endDate
                        if (startDate.after(endDate)){
                            Toast.makeText(dialog.getContext(),"End date must be after start date",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int maxScore;
                        try{
                            maxScore = Integer.parseInt(scoreMaxText.getText().toString());
                        }catch (NumberFormatException e){
                            maxScore = -1;
                        }
                        int maxID = scoreMaxRG.getCheckedRadioButtonId();
                        String maxRG = ((RadioButton) dialog.findViewById(maxID)).getText().toString();
                        int minScore;
                        try{
                            minScore = Integer.parseInt(scoreMinText.getText().toString());
                        }catch (NumberFormatException e){
                            minScore = -1;
                        }
                        int minID = scoreMinRG.getCheckedRadioButtonId();
                        String minRG = ((RadioButton) dialog.findViewById(minID)).getText().toString();
                        filterHandler(team,stadium,league,startDate,endDate,maxScore,maxRG,minScore,minRG);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            case R.id.action_search:
                //search popup
                dialog.setContentView(R.layout.search_popup);
                dialog.setTitle("Search");

                EditText editText = (EditText) dialog.findViewById(R.id.search_popup_input);
                confirm = (Button) dialog.findViewById(R.id.search_popup_confirm);
                cancel = (Button) dialog.findViewById(R.id.search_popup_cancel);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //call handler, then dismiss
                        String searchParam = editText.getText().toString();
                        searchHandler(searchParam);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            case R.id.action_sort:
                String[] options = {
                        "Road Team",
                        "Home Team",
                        "League",
                        "Stadium Name",
                        "Date",
                        "Road Score",
                        "Home Score"
                };

                dialog.setContentView(R.layout.sort_popup);
                dialog.setTitle("Sort");

                //set the spinner options for sort
                Spinner spinner = (Spinner) dialog.findViewById(R.id.sort_popup_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                //get the radio group and confirm/cancel buttons
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.sort_popup_radio_group);
                confirm = (Button) dialog.findViewById(R.id.sort_popup_confirm);
                cancel = (Button) dialog.findViewById(R.id.sort_popup_cancel);
                //set the onClick for buttons
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //call handler, then dismiss
                        int id = radioGroup.getCheckedRadioButtonId();
                        if (id == -1){
                            Toast.makeText(getApplicationContext(),"Please select either Asc. or Dsc.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        RadioButton radioButton = (RadioButton) dialog.findViewById(id);
                        String sp = (String) spinner.getSelectedItem();
                        String rg = radioButton.getText().toString();
                        sortHandler(sp,rg);
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //show the dialog
                dialog.show();
                return true;
            case R.id.action_refresh:
                events.clear();
                events.addAll(allEvents);
                visitListAdapter = new VisitListAdapter(events,this,user);
                listView.setAdapter(visitListAdapter);
                return true;
            default:
                //Back button pressed
                Intent backIntent = new Intent(this,MainMenuActivity.class);
                backIntent.putExtra("user", user);
                startActivity(backIntent);
                return true;

        }
    }
    /*John Strauser
        given search parameter
        If parameter exists in an event, keep it in the listview
        otherwise remove it
        if no results exist from the search, undo the search
     */
    public void searchHandler(String param){
        List<Event> temp = new ArrayList<>();
        temp.addAll(events);

        events.clear();
        for (Event event: temp){
            if (event.getRoadTeam().getNickname().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getHomeTeam().getNickname().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getRoadTeam().getCity().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getHomeTeam().getCity().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getStadium().getName().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getStadium().getCity().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getStadium().getCountry().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else if (event.getLeague().toLowerCase().contains(param.toLowerCase())){
                events.add(event);
            }else{
                //Try numbers
                try{
                    int paramInt = Integer.parseInt(param);
                    if (event.getHomeScore() == paramInt){
                        events.add(event);
                    }else if (event.getRoadScore() == paramInt){
                        events.add(event);
                    }
                }catch (NumberFormatException e){
                    Log.e("search numbers",e.toString());
                }
            }
        }

        if (events.size() == 0){
            events.addAll(temp);
            Toast.makeText(this, "Search provided no results", Toast.LENGTH_SHORT).show();
        }
        visitListAdapter = new VisitListAdapter(events,this,user);
        listView.setAdapter(visitListAdapter);
    }
    /*John Strauser
        take in what to sort by and what direction to sort
        use list sort to sort the list with a comparator
     */
    public void sortHandler(String param, String dir){
        events.sort(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                if (param.equals("Stadium Name")){
                    int comp = o1.getStadium().getName().compareTo(o2.getStadium().getName());
                    if (dir.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (param.equals("Date")){
                    int comp = (o1.getDate().before(o1.getDate()))?1:-1;
                    if (dir.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (param.equals("Home Team")){
                    int comp = o1.getHomeTeam().getNickname().compareTo(o2.getHomeTeam().getNickname());
                    if (dir.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (param.equals("Road Team")){
                    int comp = o1.getRoadTeam().getNickname().compareTo(o2.getRoadTeam().getNickname());
                    if (dir.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (param.equals("League")){
                    int comp = o1.getLeague().compareTo(o2.getLeague());
                    if (dir.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (param.equals("Home Score")){
                    if (dir.equals("Ascending")){
                        if (o1.getHomeScore() == o2.getHomeScore()){
                            return 0;
                        }else if (o1.getHomeScore() > o2.getHomeScore()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }else{
                        if (o1.getHomeScore() == o2.getHomeScore()){
                            return 0;
                        }else if (o1.getHomeScore() > o2.getHomeScore()){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                }else if (param.equals("Road Score")){
                    if (dir.equals("Ascending")){
                        if (o1.getRoadScore() == o2.getRoadScore()){
                            return 0;
                        }else if (o1.getRoadScore() > o2.getRoadScore()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }else{
                        if (o1.getRoadScore() == o2.getRoadScore()){
                            return 0;
                        }else if (o1.getRoadScore() > o2.getRoadScore()){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                }
                return 0;
            }
        });
        visitListAdapter = new VisitListAdapter(events,this,user);
        listView.setAdapter(visitListAdapter);
    }
    /*John Strauser
        take in all filters
        if a list entry doesnt match a filter, remove it from the list
        if no results exist, reset the list to before the filtering
     */
    public void filterHandler(String team,String stadium,String league,Calendar startDate,Calendar endDate,int maxScore,String maxRG,int minScore,String minRG){
        List<Event> temp = new ArrayList<>();
        temp.addAll(events);

        List<Event> toRemove = new ArrayList<>();
        for (Event event : events){
            if (!team.equals("Team") && !event.getHomeTeam().getNickname().equals(team) && !event.getRoadTeam().getNickname().equals(team)){
                Log.e("team","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                toRemove.add(event);
            }else if (!stadium.equals("Stadium") && !event.getStadium().getName().equals(stadium)){
                Log.e("stadium","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                toRemove.add(event);
            }else if (!league.equals("League") && !event.getLeague().equals(league)){
                Log.e("league","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                toRemove.add(event);
            }else if (event.getDate().before(startDate) || event.getDate().after(endDate)) {
                Log.e("date","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                toRemove.add(event);
            }else if (maxScore != -1){
                //Max score
                if (maxRG.equals("Individual")){
                    if (event.getHomeScore() > maxScore && event.getRoadScore() > maxScore){
                        Log.e("max","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                        Log.e("max","max="+maxScore+" home="+event.getHomeScore()+" road="+event.getRoadScore());
                        toRemove.add(event);
                    }
                }else {
                    if (event.getHomeScore()+event.getRoadScore() > maxScore){
                        Log.e("max","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                        toRemove.add(event);
                    }
                }
            }else if (minScore != -1){
                //Min Score
                if (minRG.equals("Individual")){
                    if (event.getHomeScore() < minScore && event.getRoadScore() < minScore){
                        Log.e("min","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                        toRemove.add(event);
                    }
                }else {
                    if (event.getHomeScore()+event.getRoadScore() < minScore){
                        Log.e("min","removed "+event.getRoadTeam()+" @ "+event.getHomeTeam());
                        toRemove.add(event);
                    }
                }
            }
        }

        events.removeAll(toRemove);
        if (events.size() == 0){
            events.addAll(temp);
            Toast.makeText(this, "Filter provided no results", Toast.LENGTH_SHORT).show();
        }
        visitListAdapter = new VisitListAdapter(events,this,user);
        listView.setAdapter(visitListAdapter);
    }
}
