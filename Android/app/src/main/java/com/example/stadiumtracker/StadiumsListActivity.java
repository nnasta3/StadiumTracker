package com.example.stadiumtracker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.stadiumsCountForUser;
import com.example.stadiumtracker.helpers.StadiumListAdapter;
import com.example.stadiumtracker.helpers.StadiumListHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    TODO List for this file:
        1. Add refresh button to toolbar (Maybe)
        2. Move code for populating the listView into own method (for refresh button)
        3. Share button
        4. Filter button
        (optional) add more parameters for searching this list
 */

public class StadiumsListActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;
    ListView listView;
    List<Stadium> stadiums;
    List<StadiumListHelper> stadiumListHelpers, partialStadiumListHelpers;
    StadiumListAdapter stadiumListAdapter;

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

        //initialize information in the listView
        //TODO: possibly replace with a call to populateList to allow refresh button
        try{
            stadiums = new allStadiums(this).execute().get();
        }catch (Exception e){
            Log.w("error allStadiums",e.toString());
        }
        //number of visits to each stadium for this user (defaulting to zero)
        Map<Integer, Integer> counts = new HashMap<>();
        try{
            counts = new stadiumsCountForUser(this).execute(user.getUserID()).get();
        }catch (Exception e){
            Log.w("error stadiumsCount", e.toString());
        }
        //create list of helper class
        stadiumListHelpers = new ArrayList<>();
        for (int i=0; i<stadiums.size(); i++){
            //enter the counts
            if (counts.containsKey(stadiums.get(i).getStadiumID())){
                stadiumListHelpers.add(new StadiumListHelper(stadiums.get(i),user,counts.get(stadiums.get(i).getStadiumID())));
            }else {
                stadiumListHelpers.add(new StadiumListHelper(stadiums.get(i),user,0));
            }
        }
        //copy full list to partial list
        //full list is only used when there is a need to refresh the partial list for things like filtering or searching
        partialStadiumListHelpers = new ArrayList<>();
        partialStadiumListHelpers.addAll(stadiumListHelpers);
        //adapt list of helper class to listView
        if (partialStadiumListHelpers.size() > 0){
            stadiumListAdapter = new StadiumListAdapter(this,partialStadiumListHelpers,user);
            listView.setAdapter(stadiumListAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stadiums_list_menu, menu);
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

    public void populateList(){
        
    }
    public void filterHandler(){
        //TODO: implement filter
    }

    public void searchHandler(String param){
        List<StadiumListHelper> temp = new ArrayList<>();
        temp.addAll(partialStadiumListHelpers);
        partialStadiumListHelpers.clear();
        for(int i=0; i<stadiumListHelpers.size(); i++){
            if (stadiumListHelpers.get(i).getStadium().getName().toLowerCase().contains(param.toLowerCase())){
                partialStadiumListHelpers.add(stadiumListHelpers.get(i));
            }else if (stadiumListHelpers.get(i).getStadium().getCity().toLowerCase().contains(param.toLowerCase())){
                partialStadiumListHelpers.add(stadiumListHelpers.get(i));
            }else if (stadiumListHelpers.get(i).getStadium().getCountry().toLowerCase().contains(param.toLowerCase())){
                partialStadiumListHelpers.add(stadiumListHelpers.get(i));
            }
            //TODO: possibly add more search params
        }
        if (partialStadiumListHelpers.size() == 0){
            partialStadiumListHelpers.addAll(temp);
            Toast.makeText(this, "Search provided no results", Toast.LENGTH_SHORT).show();
        }
        stadiumListAdapter = new StadiumListAdapter(this,partialStadiumListHelpers,user);
        listView.setAdapter(stadiumListAdapter);
    }

    public void sortHandler(String sp, String rg){
        //sp = spinner selection in string form
        //rg = radio group selection in string form (asc/dsc)
        partialStadiumListHelpers.sort(new Comparator<StadiumListHelper>() {
            @Override
            public int compare(StadiumListHelper o1, StadiumListHelper o2) {
                if (sp.equals("Stadium Name")){
                    int comp = o1.getStadium().getName().compareTo(o2.getStadium().getName());
                    if (rg.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (sp.equals("City")){
                    int comp = o1.getStadium().getCity().compareTo(o2.getStadium().getCity());
                    if (rg.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (sp.equals("Country")){
                    int comp = o1.getStadium().getCountry().compareTo(o2.getStadium().getCountry());
                    if (rg.equals("Ascending")){
                        return comp;
                    }else{
                        return comp*(-1);
                    }
                }else if (sp.equals("League")){
                    //TODO: database changes required for this
                }else if (sp.equals("Number Of Visits")){
                    if (rg.equals("Ascending")){
                        if (o1.getVisits() == o2.getVisits()){
                            return 0;
                        }else if (o1.getVisits() > o2.getVisits()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }else{
                        if (o1.getVisits() == o2.getVisits()){
                            return 0;
                        }else if (o1.getVisits() > o2.getVisits()){
                            return -1;
                        }else{
                            return 1;
                        }
                    }
                }
                return 0;
            }
        });
        stadiumListAdapter = new StadiumListAdapter(this,partialStadiumListHelpers,user);
        listView.setAdapter(stadiumListAdapter);
    }
}
