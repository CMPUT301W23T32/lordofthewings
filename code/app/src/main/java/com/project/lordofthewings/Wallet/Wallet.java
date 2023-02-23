package com.project.lordofthewings.Wallet;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Wallet {
    private Player player;

    private int qrCodesScanned;
    private int score;

    //will be in activity, so user can click and go to that Qrcode using the listview
    private ArrayList <QrCode> qrCodes;

    public Wallet(Player player){
        this.player = player;
        this.qrCodesScanned = updateCodesScanned();
        this.score = getCodesScanned();
    }

    public void editPlayerContact(String newContact){
        player.setContact(newContact);
    }

    public void editPlayerName(String newName){
        player.setname(newName);

    }

    public void editPlayerImage(String newImage) {
        //Not needed
    }

    public void deleteQrCodeFromWallet(QrCode code){
        //use firebase API to delete from qrcode collection
        //call updateCodesScanned()
        //call updateScore()

    }

    public void addQrCodeToWallet(QrCode code){
        //use firebase API to add to qrcode collection
        //call updateCodesScanned()
        //call updateScore()
    }

    public int Ranking(){
        return 0;

    }
    public void updateCodesScanned(){
        //use count() aggregation for collection of user QR codes;
        //add value in the firestore( WILL OVERRIDE IF document key already there)
        this.qrCodesScanned = use count() aggregation for collection of user QR codes;


    }

    public void updateScore(){
        int temp;
        //user firebase api to get the value of each qr code. Add add to temp variable.
        //add value in the firestore( WILL OVERRIDE IF document key already there)

        this.score = temp;



    }

    public int getCodesScanned(){

        return this.qrCodesScanned;
    }


    public int getScore() {
        return this.score;
    }

}
