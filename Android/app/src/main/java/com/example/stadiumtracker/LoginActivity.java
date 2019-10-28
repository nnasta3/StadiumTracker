package com.example.stadiumtracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    private ProgressDialog dialog;

    private static String url_login = R.string.base_url + "login_activity"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         usernameField = (EditText) findViewById(R.id.username_login);
         passwordField = (EditText) findViewById(R.id.password_login);
    }

    public void loginHandler(View v){
        //TODO: query datatbase for usernames and emails where password = text field
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(username.length() <= 0){
            Toast.makeText(getApplicationContext(),"Please Enter A Username",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() <= 0){
            Toast.makeText(getApplicationContext(),"Please Enter A Password",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(),"Query Database would occur here",Toast.LENGTH_SHORT).show();
    }

    public void registerHandler(View v){
        //TODO: send to signup activity
        //Toast.makeText(getApplicationContext(),"Send to register page would occur here",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }

    class loginHandlerClass extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Attempting Login.");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
        protected void onPostExecute(String idkWhatToUseThisFor){
            dialog.dismiss();
        }
    }
}
