package com.project.lordofthewings.Views.StartUpPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.HomePage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * Class to handle to sign up for new users
 */
public class SignUpPage extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.signup_page);
        Log.d("Breakpoint","Reached here69");
        Button signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        signUpButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            EditText username = findViewById(R.id.username);
            EditText email = findViewById(R.id.email);
            EditText firstName = findViewById(R.id.firstName);
            EditText lastName = findViewById(R.id.lastName);

            String usernameRes = username.getText().toString();
            String emailRes = email.getText().toString();
            String firstNameRes = firstName.getText().toString();
            String lastNameRes = lastName.getText().toString();


            if (!usernameRes.equals("") && !firstNameRes.equals("") && !lastNameRes.equals("") && !emailRes.equals("")) {
                FirebaseController fbcontroller = new FirebaseController();
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestore db = fbcontroller.getDb();

                Log.d("Breakpoint", "Reached here");
                CompletableFuture<Player> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        return new Player(usernameRes, emailRes, firstNameRes, lastNameRes, db);
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(SignUpPage.this, "Username already exists!", Toast.LENGTH_SHORT).show());
                        throw new RuntimeException(e);
                    }
                });
                future.thenApply(player -> {
                        Log.d("Breakpoint", "Reached here2");
                        Map<String, Object> user = addPlayertoDB(usernameRes, emailRes, firstNameRes, lastNameRes);
                        db.collection("Users").document(usernameRes).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Success in writing", "DocumentSnapshot successfully written!");
                                    Intent intent = new Intent(SignUpPage.this, HomePage.class);
                                    SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sh.edit();
                                    editor.putString("username", usernameRes);
                                    editor.apply();
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Failure in writing", "Error writing document", e);
                                });
                    return null;
                }).exceptionally(e -> {
                    Log.e("Error in creating player","Error" , e);
                    progressBar.setVisibility(View.GONE);
                    runOnUiThread(() -> Toast.makeText(SignUpPage.this, "Username already exists!", Toast.LENGTH_SHORT).show());
                    return null;
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpPage.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
            }

        });
    }


    /**
     * Method to add a player to the database during sign up
     * @param userName
     *  username of the player
     * @param email
     *  email of the player
     * @param firstName
     *  first name of the player
     * @param lastName
     *  last name of the player
     * @return
     *  a map instance of the player object
     */
    public Map<String, Object> addPlayertoDB(String userName, String email,String firstName, String lastName){
        //if you want to use the player object
        //can add a new constructor to the player class that takes in all the parameters

//        Player player = new Player(userName);
//        player.setUserName(userName);
//        player.setFirstName(firstName);
//        player.setLastName(lastName);
//        player.setEmail(email);
        //if you want to just put values to the db
        Map<String, Object> user = new HashMap<>();
        ArrayList<QRCode> qrcodes = new ArrayList<QRCode>();
        user.put("username", userName);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("QRCodes", qrcodes);
        user.put("Score",0);
        return user;
    }
}
