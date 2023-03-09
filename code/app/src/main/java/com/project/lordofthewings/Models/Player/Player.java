package com.project.lordofthewings.Models.Player;
//import QRCode
//import wallet

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Controllers.FirebaseController;

//friends is a list of players
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Player {
    private String userName;
    private String fullName;
    private String email;
    FirebaseController fbcontroller = new FirebaseController();
    FirebaseFirestore db = fbcontroller.getDb();

    public Player(String userName){
        this.userName = userName;
        db.collection("Users").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                assert data != null;
                this.email = (String) data.get("email");
            }
        });
    }

    //method to check if the user exists asynchronously
    public CompletableFuture<Boolean> checkIfUserExists(String userName){
        FirebaseFirestore db = this.fbcontroller.getDb();
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


    //private ArrayList<QRCode>  QRList;
    //private Wallet QRWallet;
    //private Leaderboard leaderboard;

    /*
    @return: return the full Name of the player
     */
    public  String getFullName(){
        return this.fullName;
    }
    /*
    set the full Name of the player
    */
    public void setFUllName(String newName){
        this.fullName = newName;
    }

    /*
    @return: return the useName of the player
     */
    public String getUserName(){
        return this.userName;
    }
    public void setUserName (String newUserName){
        this.userName = newUserName;
    }

}