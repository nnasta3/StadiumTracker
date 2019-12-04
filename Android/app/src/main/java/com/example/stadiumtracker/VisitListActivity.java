package com.example.stadiumtracker;

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
import android.widget.EditText;
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
import com.example.stadiumtracker.helpers.VisitListAdapter;

import java.util.ArrayList;
import java.util.List;

public class VisitListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;
    List<Event> events;
    VisitListAdapter visitListAdapter;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
            case R.id.action_share:
                //TODO: share popup
                /*
                    TODO: Possible information to share when this button is selected
                        1. "user has visited:
                            [stadium] ([city],[country]) - [visits] times
                            ..."
                        2. "user has visited x out of x (based on filter) stadiums"
                 */
                return true;
            case R.id.action_filter:
                //TODO: filter popup
                /*
                    popup with dropdowns for filters
                        -city
                        -country
                        -league

                 */
                return true;
            case R.id.action_search:
                //search popup
                /*
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
                */
                return true;
            case R.id.action_sort:
                /*
                String[] options = {
                        "Stadium Name",
                        "City",
                        "Country",
                        "League",
                        "Number Of Visits"
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
                 */
                return true;
            //TODO: Add refresh list button
            default:
                //Back button pressed
                Intent backIntent = new Intent(this,MainMenuActivity.class);
                backIntent.putExtra("user", user);
                startActivity(backIntent);
                return true;

        }
    }
}
