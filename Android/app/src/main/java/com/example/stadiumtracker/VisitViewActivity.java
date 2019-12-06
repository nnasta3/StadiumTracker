package com.example.stadiumtracker;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.data.User;
import com.example.stadiumtracker.database.deleteVisit;
import com.example.stadiumtracker.helpers.MediaScanner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class VisitViewActivity extends AppCompatActivity {
    User user;
    Event event;
    Toolbar toolbar;
    TextView leagueText, homeTeamText, roadTeamText, scoresText, stadiumText, cityText, dateText;
    String from;

    private static final String[] LOCATION_PERMS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_view);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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
            case R.id.action_stub:
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.stub_popup);
                dialog.setTitle("Ticket Stub");

                //TODO: generate virtual ticket stub
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.default_stub,options);
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTypeface(Typeface.MONOSPACE);
                paint.setTextSize(50);
                int y = 150;
                //Home team
                if (event.getHomeTeam().toString().length() > 26){
                    String homeTeamCity = event.getHomeTeam().getCity();
                    String homeTeamName = event.getHomeTeam().getNickname();
                    int cityDiff = 26 - homeTeamCity.length();
                    int nameDiff = 26 - homeTeamCity.length();
                    double cityAdd = cityDiff/1.75 * 30;
                    double nameAdd = nameDiff/1.75 * 30;
                    canvas.drawText(homeTeamCity,200+(int)cityAdd,y,paint);
                    y += 50;
                    canvas.drawText(homeTeamName,200+(int)nameAdd,y,paint);
                    y += 50;
                }else{
                    String homeTeam = event.getHomeTeam().toString();
                    int diff = 26 - (homeTeam.length());
                    double add = diff/1.75 * 30;
                    Log.e("addHome",""+add);
                    canvas.drawText(homeTeam,200+(int)add,y,paint);
                    y += 50;
                }
                //vs
                String vs = "Vs.";
                canvas.drawText(vs,560,y,paint);
                y += 50;
                //road team
                if (event.getRoadTeam().toString().length() > 26){
                    String roadTeamCity = event.getRoadTeam().getCity();
                    String roadTeamName = event.getRoadTeam().getNickname();
                    int cityDiff = 26 - roadTeamCity.length();
                    int nameDiff = 26 - roadTeamName.length();
                    double cityAdd = cityDiff/1.75 * 30;
                    double nameAdd = nameDiff/1.75 * 30;
                    canvas.drawText(roadTeamCity,200+(int)cityAdd,y,paint);
                    y += 50;
                    canvas.drawText(roadTeamName,200+(int)nameAdd,y,paint);
                    y += 50;
                }else{
                    String roadTeam = event.getRoadTeam().toString();
                    int diff = 26 - roadTeam.length();
                    double add = diff/1.75 * 30;
                    Log.e("addRoad",""+add);
                    canvas.drawText(roadTeam,200+(int)add,y,paint);
                    y += 50;
                }
                //stadium
                y += 25;
                String stadium = event.getStadium().getName();
                int stadiumDiff = 26 - stadium.length();
                double stadiumAdd = stadiumDiff/1.75 * 30;
                canvas.drawText(stadium,200+(int)stadiumAdd,y,paint);
                y+= 50;
                //City, Country
                String cc = event.getStadium().getCity()+", "+event.getStadium().getCountry();
                int ccDiff = 26 - cc.length();
                double ccAdd = ccDiff/1.75 * 30;
                canvas.drawText(cc,200+(int)ccAdd,y,paint);
                y+= 50;
                //date
                y+= 25;
                String date = event.getDateFullString();
                int dateDiff = 26 - date.length();
                double dateAdd = dateDiff/1.75 * 30;
                canvas.drawText(date,200+(int)dateAdd,y,paint);

                //Display stub popup
                ImageButton close = (ImageButton) dialog.findViewById(R.id.stub_popup_close);
                ImageView image = (ImageView) dialog.findViewById(R.id.stub_popup_image);
                ImageButton share = (ImageButton) dialog.findViewById(R.id.stub_popup_share);
                ImageButton download = (ImageButton) dialog.findViewById(R.id.stub_popup_download);

                //set image view to virtual ticket stub
                image.setImageBitmap(bitmap);
                //Button listeners
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String state = Environment.getExternalStorageState();
                        if (!Environment.MEDIA_MOUNTED.equals(state)) {
                            Toast.makeText(dialog.getContext(), "Cannot Access External Storage at this time", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        Uri uri = saveBitmap(bitmap);
                        shareImage(uri);
                    }
                });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(PackageManager.PERMISSION_GRANTED==checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                            requestPermissions(LOCATION_PERMS,LOCATION_REQUEST);
                        }
                        //save to device's photo gallery
                        String fileName = event.fileString()+".png";
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
                        if (file.exists())
                            file.delete();
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new MediaScanner(dialog.getContext(),file);
                        Toast.makeText(dialog.getContext(),"Saved as "+fileName,Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
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

    private Uri saveBitmap(Bitmap image) {
        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            Log.d("save bitmap", "IOException: " + e.getMessage());
        }
        return uri;
    }
    private void shareImage(Uri uri){
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("image/png");
        startActivity(sendIntent);
    }
}
