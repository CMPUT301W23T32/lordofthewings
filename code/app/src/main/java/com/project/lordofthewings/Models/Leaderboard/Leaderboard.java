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

/**
 * represents the leaderboard for the QR game
 */
public class Leaderboard {
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();
    public ArrayList<String> leaderboard = new ArrayList<String>();

    /**
     * initializes an object representing the leaderboard
     */
    public Leaderboard(){
        createLeaderboard();
    }

    /**
     * creates the leaderboard based on the score of the players
     */
    public void createLeaderboard() {
        db
                .collection("Users")
                .orderBy("Score", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "on success: we getting data");
                        List<DocumentSnapshot> users = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : users) {
                            String playerToBeInitialized = snapshot.getString("username");
                            try {
                                Player player = new Player(playerToBeInitialized);
                                leaderboard.add(player);
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

    /**
     * Returns the ranking of a specific player
     * @param p : the player we want the global ranking of
     * @return : the ranking of the player
     */
    public int getGlobalRankingOfPlayer(Player p){
        int i;
        for (i = 0; i < leaderboard.size(); i++){
            String playerUsername = (leaderboard.get(i).getUserName());
            if (p.getUserName().equals(playerUsername)){
                break;
            }

        }
        return (i + 1);

    }

    /**
     * returns ArrayList representing the ranking of the players, lower the index = higher the
     * rating
     * @return : The ArrayList representing the ranking of the players
     */
    public ArrayList<Player> getLeaderboard(){
        return this.leaderboard;
    }





































































}
