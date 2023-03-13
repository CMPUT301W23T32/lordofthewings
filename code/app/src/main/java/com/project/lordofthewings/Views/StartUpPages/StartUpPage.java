package com.project.lordofthewings.Views.StartUpPages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lordofthewings.R;


/**
 * Class to handle the startups page which lets users sign in or sign up
 * Note - This class is not used since login was deprecated.
 *
 */
public class StartUpPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.startup_page);
        Button login = findViewById(R.id.startup_signin_button);
        Button signup = findViewById(R.id.startup_signup_button);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(StartUpPage.this, LogInPage.class);
            startActivity(intent);
        });

        signup.setOnClickListener(v -> {
            Intent intent = new Intent(StartUpPage.this, SignUpPage.class);
            startActivity(intent);
        });
    }
}
