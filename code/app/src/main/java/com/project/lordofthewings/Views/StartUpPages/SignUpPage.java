package com.project.lordofthewings.Views.StartUpPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.HomePage;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.signup_page);

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(v -> {
            EditText username = findViewById(R.id.username);
            EditText password = findViewById(R.id.password);
            EditText confirmPassword = findViewById(R.id.confirmPassword);
            EditText email = findViewById(R.id.email);
            String usernameRes = username.getText().toString();
            String passwordRes = password.getText().toString();
            String confirmPasswordRes = confirmPassword.getText().toString();
            String emailRes = email.getText().toString();

            if (passwordRes.equals(confirmPasswordRes) && !usernameRes.equals("") && !passwordRes.equals("") && !emailRes.equals("")) {
                Map<String, Object> user = new HashMap<>();
                String pass = new String(Hex.encodeHex(DigestUtils.sha256(passwordRes)));
                user.put("username", usernameRes);
                user.put("password", pass);
                user.put("email", emailRes);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                        })
                ;
            }else{
                Log.e("Error", passwordRes);
            }
        });
    }
}
