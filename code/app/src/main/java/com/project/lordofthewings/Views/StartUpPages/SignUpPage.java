package com.project.lordofthewings.Views.StartUpPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.HomePage;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SignUpPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.signup_page);
        Log.d("Breakpoint","Reached here69");
        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> {
            EditText username = findViewById(R.id.username);
            EditText email = findViewById(R.id.email);
            EditText firstName = findViewById(R.id.firstName);
            EditText lastName = findViewById(R.id.lastName);

            String usernameRes = username.getText().toString();
            String emailRes = email.getText().toString();
            String firstNameRes = firstName.getText().toString();
            String lastNameRes = lastName.getText().toString();
            Log.d("Breakpoint","Reached here42");
            if (!usernameRes.equals("") && !firstNameRes.equals("") && !lastNameRes.equals("") && !emailRes.equals("")) {
                FirebaseController fbcontroller = new FirebaseController();
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseFirestore db = fbcontroller.getDb();
                Log.d("Breakpoint","Reached here");
                //check if user exists
                CompletableFuture<Boolean> future = checkIfUserExists(usernameRes, db);
                future.thenApply(exists -> {
                    if (exists) {
                        //write a toast here that user already exists
                        Toast.makeText(SignUpPage.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                        Log.d("TEST", "User already exists");
                    }
                    else{
                        Log.d("Breakpoint","Reached here2");
                        Map<String, Object> user = addPlayertoDB(usernameRes, emailRes, firstNameRes, lastNameRes);
                        db.collection("Users").document(usernameRes).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Success in writing", "DocumentSnapshot successfully written!");
                                        Intent intent = new Intent(SignUpPage.this, HomePage.class);
                                        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sh.edit();
                                        editor.putString("username", usernameRes);
                                        editor.apply();
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Failure in writing", "Error writing document", e);
                                    }
                                });

                    }
                    return null;
                });

        }
    });
}



    //method to check if the user exists asynchronously
    public CompletableFuture<Boolean> checkIfUserExists(String userName, FirebaseFirestore db){
        CollectionReference playerRef = db.collection("Users");
        Query query = playerRef.whereEqualTo("username",userName);

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        query.get().addOnSuccessListener(querySnapshot -> {
            boolean exists =  querySnapshot.size() > 0;
            future.complete(exists);
        }).addOnFailureListener(error -> {
            future.completeExceptionally(error);
        });

        return future;
    }

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
        user.put("username", userName);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        return user;
    }
}
