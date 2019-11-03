package com.example.stadiumtracker;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameField;
    EditText emailField;
    EditText passwordField;
    EditText confirmPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = (EditText) findViewById(R.id.username_sign_up);
        emailField = (EditText) findViewById(R.id.email_sign_up);
        passwordField = (EditText) findViewById(R.id.password_sign_up);
        confirmPasswordField = (EditText) findViewById(R.id.password_confirm_sign_up);
    }

    public void signUpHandler(View v){
        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if(validateFields(username,email,password,confirmPassword)){
            //TODO: Run the insert query
        }

    }
    private boolean validateFields(String username, String email, String password, String confirmPassword){
        int userLengthMin = 3;
        int passLengthMin = 3;
        //Username length minimum
        if(username.length() < userLengthMin){
            Toast.makeText(getApplicationContext(),"Username must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //username taken
        try{
            if(new UsernameQuery().execute(username).get()){
                Toast.makeText(getApplicationContext(),"Username must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch(Exception e) {
            Log.w("Error validateFields", "" + e);
        }
        //Email pattern
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(),"Please enter a valid email",Toast.LENGTH_SHORT).show();
            return false;
        }
        //TODO: Email taken
        //Password length minimum
        if(password.length() < passLengthMin){
            Toast.makeText(getApplicationContext(),"Password must be at least 3 characters long.",Toast.LENGTH_SHORT).show();
            return false;
        }
        //Passowrds dont match
        if(!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(),"Passwords do not match.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
    class  UsernameQuery extends AsyncTask<String, Void, Boolean> {
        String ip = "database-2.ctqwj4cnvuoo.us-east-2.rds.amazonaws.com" ;
        String port = "1433";
        String dbName = "test";
        String masterUser = "admin";
        String masterPass = "password";
        @Override
        protected Boolean doInBackground(String... strings) {
            //Strings[0] = username
            try {
                // SET CONNECTIONSTRING
                Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
                Connection DbConn = DriverManager.getConnection("jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbName + ";user=" + masterUser + ";password=" + masterPass);
                Statement stmt = DbConn.createStatement();
                ResultSet rs = stmt.executeQuery("Select username from users where username='"+strings[0]+"'");
                if(rs.next()){
                    return true;
                }
                DbConn.close();
            } catch (Exception e) {
                Log.w("Error connection", "" + e);
            }
            return false;
        }
    }
}
