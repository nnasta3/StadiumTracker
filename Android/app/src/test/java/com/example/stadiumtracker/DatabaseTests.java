package com.example.stadiumtracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.stadiumtracker.data.Stadium;
import com.example.stadiumtracker.database.loginQuery;
import com.example.stadiumtracker.database.insertUser;
import com.example.stadiumtracker.database.eventsQuery;
import com.example.stadiumtracker.database.eventCreate;
import com.example.stadiumtracker.database.allStadiums;
import com.example.stadiumtracker.database.allLeagues;
import com.example.stadiumtracker.data.Event;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
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

    //TODO: query for stadiumsCountForUser
    @Test
    public void testStadiumsCountForUser(){

    }

    //TODO: query for usernameQuery
    @Test
    public void testUsernameQuery(){

    }

    //TODO: query for visitCreate
    @Test
    public void testVisitCreate(){

    }

    //TODO: query for VisitsForUserToStadium
    @Test
    public void testVisitsForUserToStadium(){

    }
}