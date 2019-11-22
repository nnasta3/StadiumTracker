package com.example.stadiumtracker;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void login_successful() {
        LoginActivity la = new LoginActivity();

        int userID = la.dbLoginQuery("admin", "password");

        int expected = 1;

        assertEquals(expected,userID);
    }
}
