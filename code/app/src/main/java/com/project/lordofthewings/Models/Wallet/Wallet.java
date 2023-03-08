package com.project.lordofthewings.Models.Wallet;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.Player.Player;
import com.project.lordofthewings.Models.QRcode.QRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Wallet {

    private String username;
    private int score;
    private ArrayList<QRCode> qrCodes;
    private int qrCodesCount;

    private Player user;

    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();


    public Wallet(Player user){
        this.user = user;
        DocumentReference docRef = db.collection("Users").document(user.getUserName());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        username = Objects.requireNonNull(document.get("username")).toString();
                        //score = Integer.parseInt(Objects.requireNonNull(document.get("Score")).toString());
                        //qrCodesCount = Integer.parseInt(Objects.requireNonNull(document.get("numberOfQRCodes")).toString());
                        qrCodes = (ArrayList<QRCode>) document.get("QRCodes");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        qrCodesCount = getInitialQRCodeCount();
        score = getInitialScore();

    }


    public void addQRCode(String qr){
        //GET STRING NEEDED TO CREATE QR OBJECT
        //INITIALIZE QR OBJECT, null will be the string we recieve from camera controller
        QRCode whatWeInitialize = new QRCode(null);
        if (haveQRCode(whatWeInitialize) == false){
            this.qrCodesCount +=1;
            this.score += whatWeInitialize.getQRScore();
            qrCodes.add(whatWeInitialize);
            Map<String, Object> newData = new HashMap<>();
            newData.put("QRCodes", qrCodes);
            db.collection("Users").document(username)
                    .update(newData);

            //ALSO ADD TO QRCODES COLLECTION IF NOT THERE, IF THERE JUST ADD THE COMMENT

        }
        else{
            throw new RuntimeException("You have already added this QR code");




        }
        user.setScore(score);
        //or could access the firestore directly in this method and change

    }

    public void deleteQRCode(QRCode qr){
        Map<String, Object> newData = new HashMap<>();
        newData.put("QRCodes", qrCodes);

        this.qrCodesCount -=1;
        this.score -= qr.getQRScore();
        qrCodes.remove(qr);

        db.collection("Users").document(username)
                        .update(newData);

        user.setScore(score);
        //or could access the firestore directly in this method and change


    }



    public int getInitialQRCodeCount(){
        return qrCodes.size();
    }


    public int getInitialScore(){
        int score = 0;
        for (int i = 0; i < qrCodesCount; i++){
            score += ((qrCodes.get(i)).getQRScore());
            }
        return score;

    }

    public boolean haveQRCode(QRCode qr){
        for (int i =0; i < qrCodesCount; i++){
            if (((qrCodes.get(i)).getHash()) == qr.getHash()){
                return true;
            }

        }
        return false;

    }

}
