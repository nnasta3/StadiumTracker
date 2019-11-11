package com.example.stadiumtracker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.stadiumtracker.data.User;

public class MainMenuActivity extends AppCompatActivity {
    User user;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.main_menu_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getSerializableExtra("user");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void recordHandler(View v){
        Intent intent = new Intent(this,RecordActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void stadiumHandler(View v){
        Intent intent = new Intent(this,StadiumsListActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void visitHandler(View v){
        //TODO: Send to visit page
    }
    public void friendsHandler(View v){
        //TODO: Send to friends page
    }
}
