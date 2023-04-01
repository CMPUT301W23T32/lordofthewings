package com.project.lordofthewings;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Models.Wallet.Wallet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseTestController {

    FirebaseFirestore db;
    ArrayList<String> TestUsers = new ArrayList<String>();
    ArrayList<String> testQrs = new ArrayList<String>();


    public void addTestUser(String username) {
        db = FirebaseFirestore.getInstance();
        Map<String,Object> user = new HashMap<>();
        ArrayList<QRCode> qrcodes = new ArrayList<QRCode>();
        user.put("username",username);
        user.put("email",username+"@gmail.com");
        user.put("firstName",username);
        user.put("lastName",username);
        user.put("QRCodes", qrcodes);
        user.put("Score",0);

        db.collection("Users").document(username).set(user).addOnSuccessListener(aVoid -> {
            Log.d("Success","Test user added");
            TestUsers.add(username);
        }).addOnFailureListener(e -> {
            Log.d("Error","Test user not added");
            Log.d("Error",e.toString());
        });
    }

    public void deleteTestUser(String username) {
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(username).delete().addOnSuccessListener(aVoid -> {
            Log.d("Success","Test user deleted");
            TestUsers.remove(username);
        }).addOnFailureListener(e -> {
            Log.d("Error","Test user not deleted");
            Log.d("Error",e.toString());
        });
    }

    /*
    * pls dont give same qr to 2 users for testing (I cri)
    * */
    public void addQRCode(String username, String content, String lat, String lon, String comment){
        //either build the qrcode using the hash or qrcontent that you wanna pass whatever
        db.collection("Users").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) documentSnapshot.get("QRCodes");
                ArrayList<QRCode> qrCodesObj = new ArrayList<>();

                for (Map<String, Object> qrCode: qrCodes) {
                    qrCodesObj.add(new QRCode((String) qrCode.get("hash"), 2));
                }
                int score = (int) documentSnapshot.get("Score");

                Wallet wallet = new Wallet(username, qrCodesObj, score, db);
                QRCode addQr = new QRCode(content);
                wallet.addQRCode(addQr, lat, lon, comment);
                testQrs.add(addQr.getHash());
            }
        });
    }


    public void deleteQRCode(String username, String qrcontent){
        //pass the qrcontent and straight up delete it, or you can use the hash too
        QRCode qr = new QRCode(qrcontent);

        db.collection("Users").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) documentSnapshot.get("QRCodes");
                ArrayList<QRCode> qrCodesObj = new ArrayList<>();

                for (Map<String, Object> qrCode: qrCodes) {
                    qrCodesObj.add(new QRCode((String) qrCode.get("hash"), 2));
                }
                int score = (int) documentSnapshot.get("Score");

                Wallet wallet = new Wallet(username, qrCodesObj, score, db);
                wallet.deleteQRCode(qr);
            }
        });

        db.collection("QRCodes").document(qr.getHash()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                testQrs.remove(qr.getHash());
            }
        });

    }


    public void deleteQRCode2(String hash){


        db.collection("QRCodes").document(hash).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                testQrs.remove(hash);
            }
        });

    }






    public void initializeTest(){
        //can control dummy data here if you want
        addTestUser("TestUser1");
        addTestUser("TestUser2");
        addQRCode("TestUser1", "Manan's QRCode", "", "", "");

    }


    public void finishTest(){
        for(String username: TestUsers){
            deleteTestUser(username);
        }

        for(String hash: testQrs){
            deleteQRCode2(hash);
        }
    }



}
