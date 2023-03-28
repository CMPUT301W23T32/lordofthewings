package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.QRCodeArrayAdapter;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.R;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    String url = "https://api.dicebear.com/5.x/pixel-art/png?seed=";
    String username;
    ListView qrCodeList;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String savedUsername = sh.getString("username", "");
        username = getIntent().getStringExtra("username");

        ImageButton backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, LeaderBoardPage.class);
            startActivity(intent);
            finish();
        });

//        ImageButton editButton = findViewById(R.id.editIcon);
//        editButton.setOnClickListener(v -> {
//
//        });


        this.fetchDataAndRefreshUI();
        if (savedUsername.equals(username)) {
            ImageButton editButton = findViewById(R.id.editIcon);
            editButton.setVisibility(ImageButton.VISIBLE);
            qrCodeList = findViewById(R.id.qr_codes_list_profile);
            qrCodeList.setVisibility(ListView.INVISIBLE);
        }else{
            ImageButton editButton = findViewById(R.id.editIcon);
            editButton.setVisibility(ImageButton.INVISIBLE);
            qrCodeList = findViewById(R.id.qr_codes_list_profile);
            TextView myProfile = findViewById(R.id.myProfileTextView);
            myProfile.setText("Profile");
            qrCodeList.setVisibility(ListView.VISIBLE);
            fetchQRCodes();
        }
        ImageView profileImage = findViewById(R.id.profileImage);
        Picasso.get().load(url+username).into(profileImage);
        TextView text_username = findViewById(R.id.username_text);
        text_username.setText("@"+ username);

        ImageButton editButton = findViewById(R.id.editIcon);
        editButton.setOnClickListener(v -> {
            new EditProfileFragment().show(getSupportFragmentManager(), "Edit Profile");
            fetchDataAndRefreshUI();
        });

        qrCodeList.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("Reaching","check");
            QRCode qrCode = qrCodeAdapter.getItem(position);
            Intent intent = new Intent(ProfilePage.this, QRCodePage.class);
            intent.putExtra("hash", qrCode.getHash());
            startActivity(intent);
        });

    }
    public void fetchDataAndRefreshUI(){
        TextView name = findViewById(R.id.full_name_text);
        TextView score = findViewById(R.id.score_text);
        TextView rank = findViewById(R.id.rank_text);
        TextView qrcode_count = findViewById(R.id.qrcodes_text);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name.setText(document.getString("firstName") + " " + document.getString("lastName"));
                        int userScore = document.getLong("Score").intValue();
                        String scoreString = Integer.toString(userScore);
                        score.setText(scoreString);
                        rank.setText("N/A");
                        ArrayList<Map<String, Object>> qrcodes = (ArrayList<Map<String, Object>>)document.get("QRCodes");
                        int count = 0;
                        if (qrcodes != null) {
                            count = qrcodes.size();
                            qrcode_count.setText(Integer.toString(count));
                        }else{
                            qrcode_count.setText(Integer.toString(count));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public void fetchQRCodes() {
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        qrCodeList = findViewById(R.id.qr_codes_list_profile);
        qrCodeAdapter = new QRCodeArrayAdapter(this);
        qrCodeList.setAdapter(qrCodeAdapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                        Log.e("qrcode", String.valueOf(qrCodes.size()));
                        if (qrCodes != null) {
                            qrCodeAdapter.clear();
                            for (Map<String, Object> qrCode : qrCodes) {
                                String hash = qrCode.get("hash").toString();
                                qrCodeAdapter.add(new QRCode(hash, 0));
                                qrCodeAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Log.d("No Doc", "No such document");
                    }
                } else {
                    Log.d("Err", "get failed with ", task.getException());
                }
            }
        });
    }

}
