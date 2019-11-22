package com.example.stadiumtracker;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.example.stadiumtracker.database.loginQuery;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void login_successful() {

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