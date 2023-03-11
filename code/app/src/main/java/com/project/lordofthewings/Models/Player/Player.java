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
import com.project.lordofthewings.Models.Leaderboard.Leaderboard;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Models.Wallet.Wallet;

//friends is a list of players
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Player {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;

    private int globalRank;


    private int score;
    private ArrayList<QRCode>  QRList;

    private Wallet QRWallet;
    //private Leaderboard leaderboard;
    FirebaseController fbcontroller = new FirebaseController();
    FirebaseFirestore db = fbcontroller.getDb();

//    public Player(String userName){
//        this.userName = userName;
//        db.collection("Users").document(userName).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Map<String, Object> data = task.getResult().getData();
//                assert data != null;
//                this.email = (String) data.get("email");
//            }
//        });
//    }


    //use this constructor to instantiate a new player during signup
    public Player(String userName, String email, String firstName, String lastName, FirebaseFirestore db) throws Exception{
        if(checkIfUserExists(userName,db).get()){
            throw new Exception("Username already exists!");
        }
        this.score = 0;
        this.QRList = new ArrayList<QRCode>();
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.globalRank = getGlobalRank();

        //db.collection("Users").document(userName).set(this);
    }


    //use this constructor to instantiate a player that already exists in the database
    public Player(String userName) throws Exception{
        CompletableFuture<Boolean> future = checkIfUserExists(userName, db);
        Boolean exists = future.get();

        if(!exists){
            throw new Exception("Username does not exist!");
        }
        this.userName = userName;
        this.globalRank = getGlobalRank();
        db.collection("Users").document(userName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                assert data != null;
                this.email = (String) data.get("email");
                this.firstName = (String) data.get("firstName");
                this.lastName = (String) data.get("lastName");
                this.score = (int) data.get("score");
                this.QRList = (ArrayList<QRCode>) data.get("QRList");
            }
        });
    }


    public CompletableFuture<Boolean> checkIfUserExists(String userName, FirebaseFirestore db){
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



    /*
    2 constructors,

    function: create a new user

    mainly for signup

    1st constructor only takes a username, checks if it exists
    if it exists then it tells the user that username is reserved
    and you gotta choose another username otherwise if it doesn't exist, add the player

    to just retrieve the values

     2nd constructor-:
     now the username should exist, no matter what and then retrieve the values

     if we want a username
     */


    public int getScore(){
        return this.score;
    }

    public int getNumOfQRCode(){
        return this.QRList.size();
    }

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

    /**
     * return the global rank of the Player
     * @return : an integer representing the global rank of the player
     */
    public int getGlobalRank(){
        Leaderboard leaderboard = new Leaderboard();
        this.globalRank = leaderboard.getGlobalRankingOfPlayer(this);

        return this.globalRank;
    }



}