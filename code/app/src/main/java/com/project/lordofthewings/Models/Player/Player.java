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
import com.google.firebase.firestore.core.FirestoreClient;
import com.project.lordofthewings.Controllers.FirebaseController;

//friends is a list of players
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Player {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;

    //private ArrayList<QRCode>  QRList;
    //private Wallet QRWallet;
    //private Leaderboard leaderboard;
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

//    public Player(String userName, String email, String firstName, String lastName){
//        this.userName = userName;
//        this.email = email;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }





    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }


    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /*
    set the full Name of the player
    */
    public void setFirstName(String firstName){
        this.firstName = firstName;
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