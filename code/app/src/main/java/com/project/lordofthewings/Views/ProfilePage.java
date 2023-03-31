package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    private String url2 = "https://api.dicebear.com/5.x/bottts-neutral/png?seed=";

    String username;
    ListView qrCodeList;
    private ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<String> qrCodes_test;
    ArrayList<Map<String, Object>> users_array;
    String savedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        SharedPreferences sh = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        savedUsername = sh.getString("username", "");
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
            LinearLayout linearLayout = findViewById(R.id.linearLayout2);
            linearLayout.setVisibility(View.VISIBLE);
            qrCodeList = findViewById(R.id.qr_codes_list_profile);
            qrCodeList.setVisibility(ListView.INVISIBLE);
        }else{
            ImageButton editButton = findViewById(R.id.editIcon);
            editButton.setVisibility(ImageButton.INVISIBLE);
            LinearLayout linearLayout = findViewById(R.id.linearLayout2);
            linearLayout.setVisibility(View.GONE);
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
        TextView ranking = findViewById(R.id.qr_ranking);
        TextView rarity = findViewById(R.id.rarity);
        TextView qr_naming = findViewById(R.id.qr_name_text);
        TextView qrcode_count = findViewById(R.id.qrcodes_text);
        ImageView qrImage = findViewById(R.id.qr_image);
        final Integer[] rank_value = {0};
        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        linearLayout.setVisibility(View.GONE);

        if (!savedUsername.equals(username)) {
            linearLayout.setVisibility(View.GONE);
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference_users = db.collection("Users");
        collectionReference_users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    users_array = new ArrayList<>(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        users_array.add(document.getData());
                    }
                    ArrayList<String> users_usernames = new ArrayList<>();

                    if (users_array != null) {
                        ArrayList<Map<String, Object>> users_array_2 = new ArrayList<>();
                        Integer count_users = users_array.size();
                        for (int i = 0; i < count_users; i++) {
                            Integer key = 0;
                            for (int j = 1; j < users_array.size(); j++) {
                                if ((int) (long) users_array.get(key).get("Score") < (int) (long) users_array.get(j).get("Score"))
                                    key = j;
                            }
                            users_array_2.add(users_array.get(key));
                            users_array.remove(users_array.get(key));
                        }
                        for (int i = 0; i < count_users; i++) {
                            users_array.add(users_array_2.get(i));
                            users_usernames.add((String) users_array_2.get(i).get("username"));
                        }

                    } else {
                        Log.d("Error", "No data present");
                    }
                    double position = ((users_usernames.indexOf(username) + 1) / (double) users_array.size()) * 100;
                    if (position >= 0 && position <= 10) {
                        rank.setText("Top 10%");
                    }
                    if (position > 10 && position <= 20) {
                        rank.setText("Top 20%");
                    }
                    if (position > 20 && position <= 30) {
                        rank.setText("Top 30%");
                    }
                    if (position > 30 && position <= 40) {
                        rank.setText("Top 40%");
                    }
                    if (position > 40 && position <= 50) {
                        rank.setText("Top 50%");
                    }
                    if (position > 50 && position <= 60) {
                        rank.setText("Top 60%");
                    }
                    if (position > 60 && position <= 70) {
                        rank.setText("Top 70%");
                    }
                    if (position > 70 && position <= 80) {
                        rank.setText("Top 80%");
                    }
                    if (position > 80 && position <= 90) {
                        rank.setText("Top 90%");
                    }
                    if (position > 90 && position <= 100) {
                        rank.setText("Top 100%");
                    }
                    System.out.println(position);

                } else {
                    Log.d("Error: ", "Couldn't get users");
                }
            }
        });

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
                        ArrayList<Map<String, Object>> qrcodes = (ArrayList<Map<String, Object>>)document.get("QRCodes");
                        Log.d("TEST:", String.valueOf(qrcodes.size()));
                        if (qrcodes.size() != 0) {
                            if (!savedUsername.equals(username)) {
                                linearLayout.setVisibility(View.GONE);
                            } else {
                                linearLayout.setVisibility(View.VISIBLE);
                            }
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
                                                            Picasso.get().load(url2+qrCodes.get(0).get("hash").toString()).into(qrImage);
                                                            QRCode qrCode = new QRCode(qrCodes.get(0).get("hash").toString(), 0);
                                                            qr_naming.setText(qrCode.getQRName());
                                                            Log.d("TEST", rank_value[0].toString());
                                                            ranking.setText(rank_value[0].toString());
                                                            double value = (rank_value[0] / (double) qrCodes_test.size()) * 100;
                                                            if (value >= 0 && value <= 10) {
                                                                rarity.setText("Very Rare");
                                                                rarity.setTextColor(Color.RED);
                                                            }
                                                            if (value > 10 && value <= 30) {
                                                                rarity.setText("Rare");
                                                                rarity.setTextColor(Color.GREEN);
                                                            }
                                                            if (value > 30 && value <= 100) {
                                                                rarity.setText("Common");
                                                                rarity.setTextColor(Color.GRAY);
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
                                    } else {
                                        Log.d("error","Error getting documents: ");
                                    }
                                }
                            });
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
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
