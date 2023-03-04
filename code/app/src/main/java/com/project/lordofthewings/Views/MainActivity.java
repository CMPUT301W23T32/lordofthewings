package com.project.lordofthewings.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseController fbcontroller = new FirebaseController();
//        FirebaseFirestore db = fbcontroller.getDb();
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        if (sh.getString("username", "").equals("")) {
            Intent intent = new Intent(MainActivity.this, StartUpPage.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);
        }
    }
}