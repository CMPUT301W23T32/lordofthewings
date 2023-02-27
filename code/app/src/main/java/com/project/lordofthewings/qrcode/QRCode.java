package com.project.lordofthewings.qrcode;


import static java.lang.Math.pow;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.HashMap;

public class QRCode {
    private final String QRContent;

    //placeholder again
    private String Location;

    private String VisualRepr;

    private String QRName;

    private Collection<String> Comments;

    //will create image attribute later

    private Integer QRScore;

    private final String QRHash;


    //placeholder in case we change it
    private String id;

    /**
     * Constructor for the Object
     * @param QRContent
     *  QRContent placeholder for now since we will retrieve that when we scan the obj
     */
    public QRCode(String QRContent){
        this.QRContent = QRContent;
        this.QRHash = this.calculateHash();
        this.VisualRepr = this.getVisualRepresentation();
        this.QRName = this.createName();
        this.QRScore = this.calculateScore();
    }

    /**
     * Getter function for QRCode ID
     * @return
     * Returns the ID
     *
     */
    public String getId(){
        return this.id;
    }

    public String getVisualRepr(){
        return this.VisualRepr;
    }

    /**
     *  Function that takes in the QRCode Content and sends out a SHA256 Hash
     *
     * @return
     *  Hashed value of that String
     */
    public String calculateHash(){
        return DigestUtils.sha256Hex(this.QRContent);
    }

    public String getHash(){
        return QRHash;
    }

    public String getHashBits(){
        return getHash().substring(0,6);
    }

    /**
     * Function that takes in the Hash value and gives out a visual representation for the QR code
     * @return
     * Visual Representation for the QR code
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
                }
                prev = i;
            }
        }
        return (Integer) score;
    }

    public Integer getQRScore(){
        return this.QRScore;
    }
}
