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
}
