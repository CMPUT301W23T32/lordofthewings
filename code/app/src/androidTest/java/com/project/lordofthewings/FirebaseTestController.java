package com.project.lordofthewings;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Models.QRcode.QRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseTestController {

    FirebaseFirestore db;
    ArrayList<String> TestUsers = new ArrayList<String>();


    public void addTestUser(String username) {
        db = FirebaseFirestore.getInstance();
        Map<String,Object> user = new HashMap<>();
        ArrayList<QRCode> qrcodes = new ArrayList<QRCode>();
        user.put("username",username);
        user.put("email",username+"@gmail.com");
        user.put("firstName",username);
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
        }).addOnFailureListener(e -> {
            Log.d("Error","Test user not deleted");
            Log.d("Error",e.toString());
        });
    }

    public void addQRCode(String username, String hash){
        //either build the qrcode using the hash or qrcontent that you wanna pass whatever
    }


    public void deleteQRCode(String qrcontent){
        //pass the qrcontent and straight up delete it, or you can use the hash too
    }



    public void initializeTest(){
        //can control dummy data here if you want
        addTestUser("TestUser1");
        addTestUser("TestUser2");
    }


    public void finishTest(){
        for(String username: TestUsers){
            deleteTestUser(username);
        }
    }



}
