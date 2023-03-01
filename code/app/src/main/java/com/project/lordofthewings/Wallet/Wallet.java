package com.project.lordofthewings.Wallet;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

    private int score;
    private ArrayList<QRCode> qrCodes;
    private int qrCodesCount;
    private FirebaseInstance db;
    private String user;


    public Wallet(){
        this.qrCodes = getUserQrCodes();
        this.db = new FirebaseController().instance;
        this.user = FireBaseController().UserId;
        this.qrCodesCount = qrCodes.size();
        this.score = getUserScore();

    }


    public void addQRCode(QRCode qr){
        //firebase and camera stuff


    }

    public void deleteQRCode(QRCode qr){
        this.qrCodesCount -=1;
        qrCodes.remove(qr);
        //firebase stuff goes here
        updateScore();
    }

    public void updateScore(){
        int temp = 0;
        for( int i = 0; i < this.qrCodesCount; i++){
            temp +=  (this.qrCodes.get(i)).getScore();
        }
        //firebase stuff goes here. Override score value
    }

    public int getUserScore(){
        //firebase stuff to get the score of the user
    }

    public ArrayList<QRCode> getUserQrCodes(){
        //firebase stuff to get the users QRCodes

    }

    public boolean checkExistingQrCode(QRCode qr){
        if (this.qrCodes.contains(qr)){
            return true;

        }
        else {
            return false;
        }

    }

}
