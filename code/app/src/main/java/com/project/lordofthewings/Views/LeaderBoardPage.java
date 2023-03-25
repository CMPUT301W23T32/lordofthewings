package com.project.lordofthewings.Views;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
//import com.project.lordofthewings.Models.Leaderboard.Leaderboard;
import com.project.lordofthewings.Controllers.PlayerArrayAdapter;
import com.project.lordofthewings.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LeaderBoardPage extends AppCompatActivity {
    ListView leaderboardList;
    ArrayAdapter<String> learderboardAdapter;
    SearchView search_bar;
    ProgressBar progressBar;
    ImageButton back;
    ArrayList<String> stringLeaderboard = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board_page);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        getLeaderboard();
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        String url = "https://api.dicebear.com/5.x/pixel-art/png?seed=";
        ImageView main_image = findViewById(R.id.profile_pic_leaderboard_main);
        Picasso.get().load(url+username).into(main_image);

        TextView main_username = findViewById(R.id.username_leaderboard_main);
        main_username.setText(username);

        sharedPreferences = getSharedPreferences("leaderboard", MODE_PRIVATE);
        String rankString = sharedPreferences.getString(username,"0");
        TextView main_rank = findViewById(R.id.position_leaderboard_main);

        main_rank.setText("#"+rankString);

        back = findViewById(R.id.backIcon2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoardPage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });


        search_bar = findViewById(R.id.search_leaderboard);
        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_bar.onActionViewExpanded();
            }
        });
        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                learderboardAdapter.getFilter().filter(newText);
                return false;
            }
        });
        leaderboardList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(LeaderBoardPage.this, ProfilePage.class);
            intent.putExtra("username", stringLeaderboard.get(position));
            startActivity(intent);
            });

    }

    public void getLeaderboard(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        learderboardAdapter = new PlayerArrayAdapter(this);
        leaderboardList = findViewById(R.id.list_leaderboard);
        leaderboardList.setAdapter(learderboardAdapter);
        db.collection("Users")
                .orderBy("Score", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> leaderboard = queryDocumentSnapshots.getDocuments();
                        SharedPreferences sharedPreferences = getSharedPreferences("leaderboard", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        for (DocumentSnapshot l : leaderboard) {
                            editor.putString(l.getId(), String.valueOf(leaderboard.indexOf(l)+1));
                            editor.apply();
                            stringLeaderboard.add(l.getId());
                        }
                        learderboardAdapter.addAll(stringLeaderboard);
                        learderboardAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error getting documents: ", e);
                    }
                });
    }
}