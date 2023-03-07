package com.project.lordofthewings.Models.Player;
//import QRCode
//import wallet
import java.util.ArrayList;
//friends is a list of players
import java.util.Map;

public class Player {
    private String userName;
    private String fullName;
    private String id;

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

    /*
     set the player ID
     */
    private void setId(String newID){
        this.id = newID;
    }
    /*
    @return: return the player ID
     */
    public String getId(){
        return this.id;
    }

}