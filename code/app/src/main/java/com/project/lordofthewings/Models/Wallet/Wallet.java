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
 * represents a players wallet
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
     * initializes a wallet object that is associated with a specific user
     */
    public Wallet(String user, ArrayList<QRCode> qrCodes, int score){
        this.username = user;
        Log.e("This is user", qrCodes.toString());
        this.qrCodes = qrCodes;
        this.score = score;
        this.qrCodesCount = qrCodes.size();

    }

    /**
     * Adds a QR code to the users Wallet, updates relevant info afterwards
     * @param qr : the string of the qrCode that we wish to add
     */
    public void addQRCode(QRCode qr, String lat, String lon){
        this.qrCodesCount +=1;
        this.score += qr.getQRScore();
        qrCodes.add(qr);
        Map<String, Object> newData = new HashMap<>();
        newData.put("QRCodes", qrCodes);
        newData.put("Score", score);
        Log.e("This is qrcodes", qrCodes.toString());
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
                            int latInt = (int) (Integer.parseInt(lat) * 1E6);
                            int lonInt = (int) (Integer.parseInt(lon) * 1E6);
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
     * deletes a QR code from the users wallet, updates relevant info afterwards
     * @param qr: the QR code we want to delete from our wallet
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
     * @return : the total number of QR codes the player has scanned
     * in previous runs of the app
     */
    public int getInitialQRCodeCount(){
        return qrCodes.size();
    }

    /**
     * Allows us to get the total score of the QR codes scanned from previous runs of the app
     * by the player
     * @return : the total score of the QR codes the player has scanned
     *           in previous runs of the app
     */
    public int getInitialScore(){
        int score = 0;
        for (int i = 0; i < qrCodesCount; i++){
            score += ((qrCodes.get(i)).getQRScore());
            }
        return score;

    }

    /**
     * gets the players score
     * @return : the players score
     */
    public int getScore(){
        return this.score;

    }
    /**
     * gets the total number of QRCodes the player has scanned
     * @return : the number of QRCodes the player has scanned
     */
    public int getQrCodesCount(){
        return this.qrCodesCount;
    }

    /**
     * gets the QR codes the user has scanned so far
     *
     * @return : the ArrayList of the QR codes the user has scanned
     */
    public ArrayList<QRCode> getQrCodes(){
        return this.qrCodes;

    }

    /**
     * Checks whether the user has already scanned the QR code
     * @param qr : the QR code we are checking for
     * @return : a boolean indicating whether the user already has scanned the QR
     */
    public boolean haveQRCode(QRCode qr){
        if (qrCodes.contains(qr)){
            return true;
        }else {
            return false;
        }

    }
}
