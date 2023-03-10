package com.project.lordofthewings.Models.QRcode;


/**
 * Class to model a QRCode object in the game.
 * Known Issues: Cleaning up some of the constructors and redundant methods
 */

import static java.lang.Math.pow;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.HashMap;

public class QRCode {
    private final String QRContent;
    private String VisualRepr;
    private String QRName;

    private Integer QRScore;
    private final String QRHash;

    private Collection<String> comments;

    private LatLng location;

    /**
     * Primary Constructor for the QRCode Class to create the object when first scanned
     * using a camera.
     *
     * @param QRContent Contents of the QRCode scanned
     *
     */
    public QRCode(String QRContent){
        this.QRContent = QRContent;
        this.QRHash = this.calculateHash();
        this.VisualRepr = this.getVisualRepresentation();
        this.QRName = this.createName();
        this.QRScore = this.calculateScore();
    }

    /**
     * Secondary Constructor for the QRCode Class to recreate an already scanned QRCode using a
     * known hash value.
     * @param hash Hash value of the QRCode
     *
     * @param location Location of the QRCode (if provided)
     */

    public QRCode(String hash, LatLng location){
        this.QRContent = "";
        this.QRHash = hash;
        this.VisualRepr = this.getVisualRepresentation();
        this.QRName = this.createName();
        this.QRScore = this.calculateScore();
        this.location = location;
    }

    /**
     * Constructor to recreate a QRCode object from the database
     * @param hash Hash value of the QRCode
     * @param a Dummy parameter to differentiate from the other constructor
     */
    public QRCode(String hash, int a){
        this.QRHash = hash;
        this.QRContent = "";
        this.QRName = this.createName();
        this.QRScore = this.calculateScore();
    }


    /**
     *  Method that takes in the QRCode Content and Calculates a SHA256 Hash
     *
     * @return
     *  SHA256-Hash Value of that QRCode
     */
    public String calculateHash(){
        String pass = new String(Hex.encodeHex(DigestUtils.sha256(this.QRContent)));
        return pass;
    }

    /**
     * Getter for the hash value of the QRCode
     * @return Hash value of the QRCode
     */
    public String getHash(){
        return QRHash;
    }


    /**
     * Extracts the first 6 characters of the Hash value
     * @return First 6 characters of the Hash value
     */

    public String getHashBits(){
        return getHash().substring(0,6);
    }

    /**
     * Method that takes in the Hash value and gives out a visual representation for the QR code
     * Uses the first 6 values of the Hash value and makes a visual representation based on that
     * @return
     * Visual Representation for the QR code in a String
     */
    public String getVisualRepresentation() {
        String name = getHashBits();
        String visual = " ------- \n" +
                        " |i   i| \n" +
                        "e|     |e\n" +
                        " |  n  | \n" +
                        " |  m  | \n" +
                        " ------- \n";
        String eyes = "Oo-|";
        String ears = "3@c*";
        String nose = ".|3@";
        String mouth = "_-w~";
        String first = "abcdefgh";
        String second = "ijklmnopq";
        String third = "rstuvwxyz";

        for (int i = 0; i<4; i++){
            char letter = name.charAt(i);
            if (Character.isDigit(name.charAt(i))) {
                visual = visual.replace('i', eyes.charAt(0));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('i', eyes.charAt(1));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('i', eyes.charAt(2));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('i', eyes.charAt(3));
            } else {
                visual = visual.replace('i', eyes.charAt(0));
            }
        }

        for (int i = 0; i<4; i++){
            char letter = name.charAt(i);
            if (Character.isDigit(name.charAt(i))) {
                visual = visual.replace('e', ears.charAt(0));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('e', ears.charAt(1));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('e', ears.charAt(2));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('e', ears.charAt(3));
            } else {
                visual = visual.replace('e', ears.charAt(0));
            }
        }

        for (int i = 0; i<4; i++){
            char letter = name.charAt(i);
            if (Character.isDigit(name.charAt(i))) {
                visual = visual.replace('m', mouth.charAt(0));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('m', mouth.charAt(1));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('m', mouth.charAt(2));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('m', mouth.charAt(3));
            } else {
                visual = visual.replace('m', mouth.charAt(0));
            }
        }

        for (int i = 0; i<4; i++){
            char letter = name.charAt(i);
            if (Character.isDigit(name.charAt(i))) {
                visual = visual.replace('n', nose.charAt(0));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('n', nose.charAt(1));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('n', nose.charAt(2));
            } else if (first.indexOf(letter) != -1) {
                visual = visual.replace('n', nose.charAt(3));
            } else {
                visual = visual.replace('n', nose.charAt(0));
            }
        }

        return visual;
    }

    public String getVisualRepr(){
        return this.VisualRepr;
    }

    /**
     *  Method to provide every QRCode to have a Human Readable Identifier.
     *  Takes the first 6 bits of the Hash Value and assigns every QR a name based on the
     *  hash value of those bits.
     * @return
     *  String with a generated Human-Readable Name
     */

    public String createName(){
        // dict can be expanded later, or outright changed
        String[] names = {"Vex", "Kron", "Zax", "Hex", "Nyx", "Onyx", "Pyro", "Cyro",
                "Volt", "Zolt", "Kinet", "Lumin", "Aero", "Crim", "Nebu", "Plasm"};
        String hashbit = getHashBits();
        StringBuilder qrNameBuilder = new StringBuilder();
        for (int i = 0;i<hashbit.length();i++){
            char bit = hashbit.charAt(i);
            qrNameBuilder.append(names[Character.getNumericValue(bit)]);
        }
        return qrNameBuilder.toString();
    }

    public String getQRName(){
        return this.QRName;
    }


    //returns the Integer type, got confused with primitive during testing, will have to be careful


    /**
     * Method to calculate the Score associated with a QRCode based on the provided system.
     * @return
     * Integer value based on the corresponding score of the QR Hash
     */
    public Integer calculateScore(){
        int score = 0;
        int prev = 0;
        int curr = 0;
        HashMap<Character, Integer> point;
        point = new HashMap<Character, Integer>()
        {{  put('0', 20);
            put('1', 1);
            put('2', 2);
            put('3', 3);
            put('4', 4);
            put('5', 5);
            put('6', 6);
            put('7', 7);
            put('8', 8);
            put('9', 9);
            put('a', 10);
            put('b', 11);
            put('c', 12);
            put('d', 13);
            put('e', 14);
            put('f', 15);
        }};

        for (int i = 0; i < QRHash.length(); i++) {
            curr = i;
            if (QRHash.charAt(prev) != QRHash.charAt(curr)){
                int sublen = curr - prev;
                if (sublen > 1){
                    score += (int) pow(point.get(QRHash.charAt(prev)), sublen-1);
                } else if ((sublen == 1) && (QRHash.charAt(prev) == '0')) {
                    score += 1;
                }
                prev = i;
            }
        }
        return (Integer) score;
    }

    public Integer getQRScore(){
        return this.QRScore;
    }

    public LatLng getLocation() {
        return location;
    }

}
