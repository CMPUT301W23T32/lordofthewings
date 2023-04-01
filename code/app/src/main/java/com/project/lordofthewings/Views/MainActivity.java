package com.project.lordofthewings.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.StartUpPages.NewStartUpPage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.StartUpPages.StartUpPage;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseController fbcontroller = new FirebaseController();
//        FirebaseFirestore db = fbcontroller.getDb();
        SharedPreferences sh = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        if (sh.getString("username", "").equals("")) {
           //Intent intent = new Intent(MainActivity.this, SignUpPage.class);
            Intent intent = new Intent(MainActivity.this, NewStartUpPage.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }
}