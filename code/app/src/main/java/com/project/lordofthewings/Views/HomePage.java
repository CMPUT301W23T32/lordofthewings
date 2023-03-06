package com.project.lordofthewings.Views;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lordofthewings.R;

public class HomePage extends AppCompatActivity {
    TextView usernametext;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.home_page);
        //TextView hometext = findViewById(R.id.TextView01);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sh.getString("username", "");
        //hometext.setText("Welcome " + username + "!");
        usernametext = findViewById(R.id.usernameTextView);
        usernametext.setText("Welcome " +  username + "!");
    }
}
