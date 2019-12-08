package com.example.stadiumtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
    /* John Strauser
        These values are for the permissions request popup to save the virtual ticket stub to the device
        No clue why 1337 and +3 are needed, but they didn't work with other values
     */
    private static final String[] PERMS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int REQUEST=INITIAL_REQUEST+3;

    /* John Strauser
        Called on initial startup of VisitViewActivity
        Steps:
            1. StrictMode allows the app to temporarily store the virtual ticket stub on the device for sharing
            2. Get all the ui components via findViewByID()
            3. Get the information from the intent that started the activity
            4. Setup the toolbar
            5. Set all the information in the page
     */
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

    /* John Strauser
        Automatically called by android when the toolbar is created in onCreate()
        Gets the options menu button from xml
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /* John Strauser
        switch-case statement for each button in the toolbar
        action_logout:
            Logout Button
            sends back to LoginActivity
        action_share:
            Share Button
            Asks user if they want to share the virtual ticket stub as well via popup dialog
            Sends to shareHandler with boolean indicating user's choice from dialog
        action_delete:
            Delete Button
            Deletes the current Visit from the database
            Sends user back to either the StadiumView or VisitList depending on where they came from
        action_stub:
            Virtual Ticket Stub
            Generates and displays the virtual ticket stub for this event in a popup dialog
            That popup allows the user to either share or save the image
        default:
            Back button (left side of toolbar)
            Sends user back to either the StadiumView or VisitList depending on where they came from
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                //Share a string containing username, stadium, number of visits, and most recent visit
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                new AlertDialog.Builder(this)
                        .setTitle("Share Ticket Stub?")
                        .setMessage("Would you like to share the virtual ticket stub for this event as well?")
                        .setIcon(R.drawable.share_icon)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                shareHandler(true);
                            }})
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                shareHandler(false);
                            }})
                        .show();
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
                Bitmap stub = generateStub();

                //Display stub popup
                ImageButton close = (ImageButton) dialog.findViewById(R.id.stub_popup_close);
                ImageView image = (ImageView) dialog.findViewById(R.id.stub_popup_image);
                ImageButton share = (ImageButton) dialog.findViewById(R.id.stub_popup_share);
                ImageButton download = (ImageButton) dialog.findViewById(R.id.stub_popup_download);

                //set image view to virtual ticket stub
                image.setImageBitmap(stub);
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
                        Uri uri = saveBitmap(stub);
                        shareImage(uri);
                    }
                });

                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(PackageManager.PERMISSION_GRANTED==checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                            requestPermissions(PERMS,REQUEST);
                        }
                        //save to device's photo gallery
                        String fileName = event.fileString()+".png";
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
                        if (file.exists())
                            file.delete();
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            stub.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
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

    /* John Strauser
        onClick function for the user clicking the stadium name in the UI
        Sends user to StadiumViewActivity
     */
    public void toStadium(View v){
        Intent intent = new Intent(this,StadiumViewActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("stadium",event.getStadium());
        intent.putExtra("numVisits",0);
        intent.putExtra("event",event);
        intent.putExtra("from","visitView");
        startActivity(intent);
    }
    /* John Strauser
        Called by the share button in the toolbar
        Generates a string of text to share
        If the user indicated they want to share the virtual ticket stub as well, that stub is generated
        Information is send to a Share Sheet, where the application gives up control to Android
     */
    private void shareHandler(boolean stub){
        String shareString = user.getName()+" attended the game between the "+event.getHomeTeam()+" and the "+event.getRoadTeam()+" at "+event.getStadium().getName()+" on "+event.getDateFullString()+". ";
        if (event.getHomeScore() > event.getRoadScore()){
            shareString += "The "+event.getHomeTeam().getNickname()+" won the game by a score of "+event.getHomeScore()+" - "+event.getRoadScore();
        }else if (event.getRoadScore() > event.getHomeScore()){
            shareString += "The "+event.getRoadTeam().getNickname()+" won the game by a score of "+event.getRoadScore()+" - "+event.getHomeScore();
        }else{
            shareString += "The game resulted in a tie with a score of "+event.getHomeScore();
        }

        if (stub){
            Bitmap ticket = generateStub();
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Toast.makeText(this, "Cannot Access External Storage at this time", Toast.LENGTH_SHORT).show();
            }
            Uri uri = saveBitmap(ticket);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.setType("*/*");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareString);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    }

    /* John Strauser
        Generates the virtual ticket stub
        Creates a bitmap from the default image, then paints texts on top
        Locations of text are calculated based on pixel measurements I performed on the default image
        The X/1.75 * 30 is an example of these measurements
        After the text is done being painted the bitmap is returned
     */
    private Bitmap generateStub(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.default_stub,options);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(50);
        int y = 150;
        int x = 200;
        int yDiff = 50;
        int stringMax = 26;
        //Home team
        if (event.getHomeTeam().toString().length() > stringMax){
            String homeTeamCity = event.getHomeTeam().getCity();
            String homeTeamName = event.getHomeTeam().getNickname();
            int cityDiff = stringMax - homeTeamCity.length();
            int nameDiff = stringMax - homeTeamCity.length();
            double cityAdd = cityDiff/1.75 * 30;
            double nameAdd = nameDiff/1.75 * 30;
            canvas.drawText(homeTeamCity,x+(int)cityAdd,y,paint);
            y += yDiff;
            canvas.drawText(homeTeamName,x+(int)nameAdd,y,paint);
            y += yDiff;
        }else{
            String homeTeam = event.getHomeTeam().toString();
            int diff = stringMax - (homeTeam.length());
            double add = diff/1.75 * 30;
            Log.e("addHome",""+add);
            canvas.drawText(homeTeam,x+(int)add,y,paint);
            y += yDiff;
        }
        //vs
        String vs = "Vs.";
        canvas.drawText(vs,560,y,paint);
        y += yDiff;
        //road team
        if (event.getRoadTeam().toString().length() > stringMax){
            String roadTeamCity = event.getRoadTeam().getCity();
            String roadTeamName = event.getRoadTeam().getNickname();
            int cityDiff = stringMax - roadTeamCity.length();
            int nameDiff = stringMax - roadTeamName.length();
            double cityAdd = cityDiff/1.75 * 30;
            double nameAdd = nameDiff/1.75 * 30;
            canvas.drawText(roadTeamCity,x+(int)cityAdd,y,paint);
            y += yDiff;
            canvas.drawText(roadTeamName,x+(int)nameAdd,y,paint);
            y += yDiff;
        }else{
            String roadTeam = event.getRoadTeam().toString();
            int diff = stringMax - roadTeam.length();
            double add = diff/1.75 * 30;
            Log.e("addRoad",""+add);
            canvas.drawText(roadTeam,x+(int)add,y,paint);
            y += yDiff;
        }
        //stadium
        y += (yDiff/2);
        String stadium = event.getStadium().getName();
        int stadiumDiff = stringMax - stadium.length();
        double stadiumAdd = stadiumDiff/1.75 * 30;
        canvas.drawText(stadium,x+(int)stadiumAdd,y,paint);
        y+= yDiff;
        //City, Country
        String cc = event.getStadium().getCity()+", "+event.getStadium().getCountry();
        int ccDiff = stringMax - cc.length();
        double ccAdd = ccDiff/1.75 * 30;
        canvas.drawText(cc,x+(int)ccAdd,y,paint);
        y+= yDiff;
        //date
        y+= (yDiff/2);
        String date = event.getDateFullString();
        int dateDiff = stringMax - date.length();
        double dateAdd = dateDiff/1.75 * 30;
        canvas.drawText(date,x+(int)dateAdd,y,paint);

        return bitmap;
    }
    /* John Strauser
        Takes a bitmap as input and creates a temporary save location
        Uri path is returned as output
     */
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
    /* John Strauser
        Shares the image pointed to by the Uri input
        no return value, Android takes control when startActivity() is called
     */
    private void shareImage(Uri uri){
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.setType("image/png");
        startActivity(sendIntent);
    }
}
