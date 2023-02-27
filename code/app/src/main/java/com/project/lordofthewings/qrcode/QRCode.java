package com.project.lordofthewings.qrcode;

import org.apache.commons.codec.digest.DigestUtils;

public class QRCode {
    public final String QRContent;

    //placeholder in case we change it
    private String id;

    /**
     * Constructor for the Object
     * @param QRContent
     *  QRContent placeholder for now since we will retrieve that when we scan the obj
     */
    public QRCode(String QRContent){
        this.QRContent = QRContent;
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

    /**
     *  Function that takes in the QRCode Content and sends out a SHA256 Hash
     *
     * @return
     *  Hashed value of that String
     */
    public String getHash(){
        return DigestUtils.sha256Hex(this.QRContent);
    }

    /**
     * Function that takes in the SHA256 Hash value and gives out a name for the QR code
     * @return
     * Name for the QR code
     */
    public String getName(){
        return getHash().substring(0,6);
    }

    /**
     * Function that takes in the Hash value and gives out a visual representation for the QR code
     * @return
     * Visual Representation for the QR code
     */
    public String getVisualRepresentation() {
        String name = getName();
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
}
