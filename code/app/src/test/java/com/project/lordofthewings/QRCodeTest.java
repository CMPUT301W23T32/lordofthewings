package com.project.lordofthewings;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.project.lordofthewings.qrcode.QRCode;

import org.junit.jupiter.api.Test;

public class QRCodeTest {


    @Test
    void testGetHash(){
        QRCode QR1 = new QRCode("testing");
        QRCode QR2 = new QRCode("testing2");
        QRCode QR3 = new QRCode("testing");
        assertEquals(QR1.getHash(),QR3.getHash());
//        System.out.println(QR1.getQRName());
//        System.out.println(QR2.getQRName());
        assertNotEquals(QR1.getHash(),QR2.getHash());
        assertEquals(QR1.getQRScore(),QR3.getQRScore());
        assertNotEquals(QR1.getQRScore(),QR2.getQRScore());
//        System.out.println(QR2.getHash());
//        assertEquals(QR1.getHash(),QR1.CalculateHash());
//        System.out.println("Hash Value Native: " +QR2.getHash());
//        System.out.println("Hash Value Calculated: " +QR2.CalculateHash());
//        //System.out.println(QR1.getName());
//        System.out.println(QR2.getVisualRepresentation());
//        System.out.println(QR2.calculateScore());

    }
}
