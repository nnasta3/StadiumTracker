package com.example.stadiumtracker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void login_successful() {
        LoginActivity la = new LoginActivity();

        int userID = la.dbLoginQuery("admin", "password");

        int expected = 1;

        assertEquals(expected,userID);
    }
}