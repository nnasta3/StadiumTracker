package com.example.stadiumtracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.database.VisitsForUserToStadium;
import com.example.stadiumtracker.database.loginQuery;
import com.example.stadiumtracker.database.insertUser;
import com.example.stadiumtracker.database.eventsQuery;
import com.example.stadiumtracker.database.eventCreate;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.allLeagues;
import com.example.stadiumtracker.data.Event;
import com.example.stadiumtracker.database.stadiumsCountForUser;
import com.example.stadiumtracker.database.usernameQuery;
import com.example.stadiumtracker.database.visitCreate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class DatabaseTests {

    private Context context = ApplicationProvider.getApplicationContext();


    //TODO: query for allLeagues
    @Test
    public void testAllLeagues(){
        List<String> list = new ArrayList<>();
        try {
            list = new allLeagues(context).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int length = 3;
        assertTrue(length == list.size());
    }

    //TODO: query for allStadiums
    @Test
    public void testAllStadiums(){

        List<Stadium> list = new ArrayList<>();
        try {
            list = new allStadiums(context).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int length = 14;
        assertTrue(length == list.size());
    }

    //TODO: query for eventCreate
    @Test
    public void testEventCreate(){

        int newEvent = 0;
        try {
            newEvent = new eventCreate(context).execute("12","2019-10-15","17","19","1","2","MLB").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int expected = 1;

        assertEquals(expected,newEvent);
    }

    //TODO: query for eventsQuery
    @Test
    public void testEventsQuery(){

        Event newEvent = null;
        try {
            newEvent = new eventsQuery(context).execute("7","2019-10-11","16","19","1","2","MLB").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotNull(newEvent);

    }

    //TODO: query for insertUser
    @Test
    public void testInsertUser(){

        boolean userID = false;
        try {
            userID = new insertUser(context).execute("joe","password").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(userID);
    }

    //test for loginQuery
    @Test
    public void testLoginQuery() {

        int userID = 0;
        try {
            userID = new loginQuery(context).execute("admin","password").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int expected = 1;

        assertEquals(expected,userID);
    }

    //query for stadiumsCountForUser
    @Test
    public void testStadiumsCountForUser(){
        //Takes a userID in and provides a map of stadiumIDs (key) to number of visits to said stadium (value).
        Map<Integer, Integer> map = null;
        int userID = 1;
        try {
            map = new stadiumsCountForUser(context).execute(userID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Expected output would be a map non-null map. In the event the user hasn't recorded any visits yet, the map would be empty (size=0).
        assertNotNull(map);
    }

    //query for usernameQuery
    @Test
    public void testUsernameQuery(){
        //Takes a username in and checks if it is taken
        String username = "admin";
        Boolean taken = false;
        try {
            taken = new usernameQuery(context).execute(username).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //output should be true since admin exists in the database
        assertTrue(taken);
    }

    //query for visitCreate
    @Test
    public void testVisitCreate(){
        //Takes an eventID and userID in and returns a boolean for if the visit was inserted correctly
        int eventID = 2;
        int userID = 1;
        boolean out = false;
        try {
            out = new visitCreate(context).execute(eventID,userID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //output should be true once the visit is recorded
        assertTrue(out);
    }

    //query for VisitsForUserToStadium
    @Test
    public void testVisitsForUserToStadium(){
        //Takes in a userID and StadiumID and returns a list of events to that stadium that the user attended
        int userID = 1;
        int stadiumID = 1;
        List<Event> eventList = null;
        try {
            eventList = new VisitsForUserToStadium(context).execute(userID,stadiumID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //output is only null in the event of error with query. Otherwise the list is either empty or populated
        assertNotNull(eventList);
    }
}