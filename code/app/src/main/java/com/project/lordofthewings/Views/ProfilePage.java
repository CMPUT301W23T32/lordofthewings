package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProfilePage extends AppCompatActivity {
    String url = "https://api.dicebear.com/5.x/pixel-art/png?seed=";
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        username = getIntent().getStringExtra("username");
        TextView text_username = findViewById(R.id.username_text);
        text_username.setText("@"+ username);
        ImageView profilePic = findViewById(R.id.profileImage);
        Picasso.get().load(url + username ).into(profilePic);
        fetchDataAndRefreshUI();

    }

    public void fetchDataAndRefreshUI(){
        TextView name = findViewById(R.id.full_name_text);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("firstName") + " " + document.getString("lastName"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
