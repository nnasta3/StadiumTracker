package com.example.stadiumtracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.stadiumtracker.database.loginQuery;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class DatabaseTests {

    private Context context = ApplicationProvider.getApplicationContext();


    //TODO: query for allLeagues
    @Test
    public void testAllLeagues(){

    }

    //TODO: query for allStadiums
    @Test
    public void testAllStadiums(){

    }

    //TODO: query for eventCreate
    @Test
    public void testEventCreate(){

    }

    //TODO: query for eventsQuery
    @Test
    public void testEventsQuery(){

    }

    //TODO: query for insertUser
    @Test
    public void testInsertUser(){

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