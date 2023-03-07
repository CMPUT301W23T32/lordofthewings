package com.project.lordofthewings.Models.Player;
//import QRCode
//import wallet
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.FirebaseController;

import java.util.ArrayList;
//friends is a list of players
import java.util.Map;

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