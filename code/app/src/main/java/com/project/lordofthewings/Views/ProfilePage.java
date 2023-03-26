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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    ArrayList<String> qrCodes_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String savedUsername = sh.getString("username", "");
        username = getIntent().getStringExtra("username");

        ImageButton backButton = findViewById(R.id.backIcon);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilePage.this, HomePage.class);
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

    }
    public void fetchDataAndRefreshUI(){
        TextView name = findViewById(R.id.full_name_text);
        TextView score = findViewById(R.id.score_text);
        TextView rank = findViewById(R.id.rank_text);
        TextView qrcode_count = findViewById(R.id.qrcodes_text);
        final Integer[] rank_value = {0};

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(username);

        CollectionReference collectionReference = db.collection("QRCodes");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    qrCodes_test = new ArrayList<>(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        qrCodes_test.add(document.getId());
                    }
                    if (qrCodes_test != null) {
                        ArrayList<String> qrcodes_test_2 = new ArrayList<>();
                        Integer count = qrCodes_test.size();
                        for (int i = 0; i < count; i++) {
                            Integer key = 0;
                            for (int j = 1; j < qrCodes_test.size(); j++) {
                                if (new QRCode(qrCodes_test.get(key), 0).getQRScore() < new QRCode(qrCodes_test.get(j), 0).getQRScore())
                                    key = j;
                            }
                            qrcodes_test_2.add(qrCodes_test.get(key));
                            qrCodes_test.remove(qrCodes_test.get(key));
                        }
                        for (int i = 0; i < count; i++) {
                            qrCodes_test.add(qrcodes_test_2.get(i));
                        }
                    }
                    Log.d("DATA: ", qrCodes_test.get(1));
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.e("data", document.get("QRCodes").toString());
                                    ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                                    ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                                    if (qrCodes != null) {
                                        Integer count = qrCodes.size();
                                        for (int i = 0; i < count; i++) {
                                            Integer key = 0;
                                            for (int j = 1; j < qrCodes.size(); j++) {
                                                if (new QRCode(qrCodes.get(key).get("hash").toString(), 0).getQRScore() < new QRCode(qrCodes.get(j).get("hash").toString(), 0).getQRScore())
                                                    key = j;
                                            }
                                            qrCodes2.add(qrCodes.get(key));
                                            qrCodes.remove(qrCodes.get(key));
                                        }
                                        for (int i = 0; i < count; i++) {
                                            qrCodes.add(qrCodes2.get(i));
                                        }
                                        rank_value[0] = (Integer) (qrCodes_test.indexOf(qrCodes.get(0).get("hash"))) + 1;
                                        Log.d("TEST", rank_value[0].toString());
                                        rank.setText(rank_value[0].toString());
                                    }
                                } else {
                                    Log.d("No Doc", "No such document");
                                }
                            } else {
                                Log.d("Err", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("error","Error getting documents: ");
                }
            }
        });

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
