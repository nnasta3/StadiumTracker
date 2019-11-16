package com.example.stadiumtracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.data.Visit;
import com.example.stadiumtracker.database.VisitsForUserToStadium;
import com.example.stadiumtracker.helpers.StadiumViewAdapter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class StadiumViewActivity extends AppCompatActivity {
    User user;
    Stadium stadium;
    int numVisits;
    Toolbar toolbar;
    ImageView imageView;
    TextView nameText, cityText, visitsText;
    ListView listView;
    List<Event> events;
    StadiumViewAdapter stadiumViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium_view);

        toolbar = (Toolbar) findViewById(R.id.stadium_view_toolbar);
        imageView = (ImageView) findViewById(R.id.stadium_view_image);
        nameText = (TextView) findViewById(R.id.stadium_view_name);
        cityText = (TextView) findViewById(R.id.stadium_view_city);
        visitsText = (TextView) findViewById(R.id.stadium_view_num_visits);
        listView = (ListView) findViewById(R.id.stadium_view_list_view);

        user = (User) getIntent().getSerializableExtra("user");
        stadium = (Stadium) getIntent().getSerializableExtra("stadium");
        numVisits = (int) getIntent().getIntExtra("numVisits",0);

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //init image
        try{
            Drawable drawable = new ImageFromURL().execute(stadium.getImageURL()).get();
            if (drawable != null){
                //Get from URL was successful, replace default image
                imageView.setImageDrawable(drawable);
            }else {
                Toast.makeText(this,"null",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.e("getImage create",e.toString());
        }

        //init text views
        nameText.setText(stadium.getName());
        String temp = stadium.getCity()+", "+stadium.getCountry();
        cityText.setText(temp);
        temp = numVisits+" visits";
        visitsText.setText(temp);

        //init list view
        try{
            events = new VisitsForUserToStadium(this).execute(user.getUserID(),stadium.getStadiumID()).get();
        }catch (Exception e){
            Log.e("stadViewListInit",e.toString());
            events = null;
        }

        if (events != null){
            if (events.size() > 0){
                stadiumViewAdapter = new StadiumViewAdapter(this,events);
                listView.setAdapter(stadiumViewAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stadium_view_menu, menu);
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
                //TODO: handle sharing
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class  ImageFromURL extends AsyncTask<String, Void, Drawable>{
        @Override
        protected Drawable doInBackground(String... strings) {
            try{
                InputStream inputStream = (InputStream) new URL(strings[0]).getContent();
                return Drawable.createFromStream(inputStream,"image");
            }catch (Exception e){
                Log.e("getImage",e.toString());
                return null;
            }
        }
    }
}
