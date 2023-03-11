package com.project.lordofthewings.Models.Leaderboard;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Player.Player;

import java.util.ArrayList;
import java.util.List;
public class Leaderboard {
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();
    private ArrayList<Player> leaderboard = new ArrayList<Player>();
    private int totalPlayers = 0;

    public Leaderboard(){
        createLeaderboard();


    }

    public void createLeaderboard() {
        db
                .collection("Users")
                .orderBy("Score")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> users = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : users) {
                            String playerToBeInitialized = snapshot.getString("username");
                            try {
                                Player player = new Player(playerToBeInitialized);
                                leaderboard.add(player);
                                totalPlayers += 1;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);

                    }
                });

    }


































































}
