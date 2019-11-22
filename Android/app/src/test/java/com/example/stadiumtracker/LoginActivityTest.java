package com.example.stadiumtracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.stadiumtracker.database.loginQuery;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

public class LoginActivityTest {

    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void loginHandler() {

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
    }