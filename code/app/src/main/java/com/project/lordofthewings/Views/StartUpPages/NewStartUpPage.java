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
public class NewStartUpPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.new_startup_page);
        //Button login = findViewById(R.id.startup_signin_button);
        Button start = findViewById(R.id.start_button);

        start.setOnClickListener(v -> {
            Intent intent = new Intent(NewStartUpPage.this, SignUpPage.class);
            startActivity(intent);
        });
    }
}
