package com.project.lordofthewings.Models.Wallet;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.auth.User;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class to model a Player's Wallet in the game.
 * Purpose of the Wallet is to hold the QRCodes that the player has collected throughout.
 * Issues: Pending integration for some of the methods
 *
 */
public class Wallet {

    private String username;
    private int score;
    private ArrayList<QRCode> qrCodes= new ArrayList<QRCode>();
    private int qrCodesCount;

    private Player user;


    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();


    /**
     * Constructor for the Wallet class
     * @param user the username of the user
     * @param qrCodes the list of QR codes that the user has collected
     * @param score the score of the user
     */
    public Wallet(String user, ArrayList<QRCode> qrCodes, int score){
        this.username = user;
        this.qrCodes = qrCodes;
        this.score = score;
        this.qrCodesCount = qrCodes.size();

    }

    /**
     * Adds a QR code to the wallet
     * @param qr the QRCode Object to be added
     * @param lat the latitude of the location where the QR code was scanned
     * @param lon the longitude of the location where the QR code was scanned
     */
    public void addQRCode(QRCode qr, String lat, String lon){
        this.qrCodesCount +=1;
        this.score += qr.getQRScore();
        qrCodes.add(qr);
        Map<String, Object> newData = new HashMap<>();
        newData.put("QRCodes", qrCodes);
        newData.put("Score", score);
        db.collection("Users").document(username)
                .update(newData);

        DocumentReference docRef = db.collection("QRCodes").document(qr.getHash());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> authors;
                        authors = (ArrayList<String>) document.get("Authors");
                        authors.add(username);
                        if (lat != null && lon != null) {
                            float latInt = (float) (Float.parseFloat(lat));
                            float lonInt = (float) (Float.parseFloat(lon));
                            GeoPoint geoPoint = new GeoPoint(latInt, lonInt);
                            task.getResult().getReference().update("Location", geoPoint);
                        }
                        task.getResult().getReference().update("Authors", authors);
                    } else {
                        Log.d(TAG, "No such document");
                        ArrayList<String> authors = new ArrayList<>();
                        ArrayList<Map<String, String>> comments = new ArrayList<>();
                        Map<String, Object> data = new HashMap<>();
                        data.put("QRCode", qr);
                        data.put("Authors", authors);
                        data.put("Comments", comments);
                        if (lat != null && lon != null) {
                            float latInt = (float) (Float.parseFloat(lat));
                            float lonInt = (float) (Float.parseFloat(lon));
                            GeoPoint geoPoint = new GeoPoint(latInt, lonInt);
                            data.put("Location", geoPoint);
                        } else{
                            data.put("Location", null);
                        }
                        db.collection("QRCodes").document(qr.getHash())
                                .set(data);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Deletes a QR code from the users wallet, updates relevant info afterwards
     * @param qr the QR code object that is to be deleted
     */
    public void deleteQRCode(QRCode qr){
        Map<String, Object> newData = new HashMap<>();
        newData.put("QRCodes", qrCodes);

        this.qrCodesCount -=1;
        this.score -= qr.getQRScore();
        qrCodes.remove(qr);

        db.collection("Users").document(username)
                        .update(newData);

        //user.setScore(score);
        //or could access the firestore directly in this method and change
        //Map<String, Object> newData = new HashMap<>();
        //        newData.put("Score", score);
        //        db.collection("Users").document(username)
        //                .update(newData);


    }


    /**
     *
     * Allows us to get the users total QR codes scanned from previous runs of the app
     * @return the total number of QR codes the player has scanned
     */
    public int getInitialQRCodeCount(){
        return qrCodes.size();
    }

    /**
     * Allows us to get the total score of the QR codes scanned from previous runs of the app
     * by the player
     * @return the total score of the QR codes the player has scanned
     *
     */
    public int getInitialScore(){
        int score = 0;
        for (int i = 0; i < qrCodesCount; i++){
            score += ((qrCodes.get(i)).getQRScore());
            }
        return score;

    }

    /**
     * getter for the player score
     * @return the players score
     */
    public int getScore(){
        return this.score;

    }
    /**
     * getter for the player's scanned QR codes count
     * @return  the number of scanned QRCodes
     */
    public int getQrCodesCount(){
        return this.qrCodesCount;
    }

    /**
     * getter for the player's scanned QR codes
     *
     * @return list of scanned QRCode objects
     */
    public ArrayList<QRCode> getQrCodes(){
        return this.qrCodes;

    }


    /**
     * Checks whether the user has already scanned the QR code
     * @param qr the QR object to verify
     * @return boolean based on result
     */
    public boolean haveQRCode(QRCode qr){
        if (qrCodes.contains(qr)){
            return true;
        }else {
            return false;
        }

    }
}
