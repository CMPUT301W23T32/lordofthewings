package com.project.lordofthewings;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.project.lordofthewings.Models.QRcode.QRCode;

import org.junit.jupiter.api.Test;

public class QRCodeTest {

    @Test
    void testGetHash(){
        QRCode QR1 = new QRCode("testing");
        QRCode QR2 = new QRCode("testing2");
        QRCode QR3 = new QRCode("testing");
        //testing for hashvalues to be same for the same content ideally
        assertEquals(QR1.getHash(),QR3.getHash());
        assertNotEquals(QR1.getHash(),QR2.getHash());

    }

    @Test
    void testGetQRScore(){
        QRCode QR1 = new QRCode("testing");
        QRCode QR2 = new QRCode("testing2");
        QRCode QR3 = new QRCode("testing");

        //testing to see if cal

        //testing for the score to be same for identical content
        assertEquals(QR1.getQRScore(),QR3.getQRScore());
        assertNotEquals(QR1.getQRScore(),QR2.getQRScore());

        //manually calculated the test score to verify the algorithm
        QRCode QR4 = new QRCode("test4hash");
        System.out.println(QR4.getHash());
        Integer Value = 89;
        assertEquals(Value,QR4.getQRScore());
    }

    @Test
    void testGetQRName(){
        QRCode QR1 = new QRCode("testing");
        QRCode QR2 = new QRCode("testing2");
        QRCode QR3 = new QRCode("testing");

        //testing for names to be the same for the "same" QRCode
        assertEquals(QR1.getQRName(),QR3.getQRName());
        assertNotEquals(QR1.getQRName(),QR2.getQRName());
    }
    @Test
    void testGetVisualRepr(){
        QRCode QR1 = new QRCode("testing");
        QRCode QR2 = new QRCode("testing2");
        QRCode QR3 = new QRCode("testing");

        //testing visual representations between QRCodes
        assertEquals(QR1.getVisualRepr(),QR3.getVisualRepr());
        assertNotEquals(QR1.getVisualRepr(),QR2.getVisualRepr());
    }

    @Test
    void testLocation(){
        QRCode QR1 = new QRCode("185f8db32271fe25f561a6fc938b2e264306ec304eda518007d1764826381969", new GeoPoint(53.523427,-113.505638));
        assertEquals(QR1.getLocation().getLatitude(),53.523427);
        assertEquals(QR1.getLocation().getLongitude(),-113.505638);
        LatLng latLng = new LatLng(QR1.getLocation().getLatitude(),QR1.getLocation().getLongitude());
        System.out.println(latLng);
    }
}


