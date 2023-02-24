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
        assertNotEquals(QR1.getHash(),QR2.getHash());
    }
}
