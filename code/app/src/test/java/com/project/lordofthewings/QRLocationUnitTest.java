package com.project.lordofthewings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Models.QRLocation.QRLocation;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.MapsActivity;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.Location;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//public class QRLocationUnitTest {
//
//    public Map<String, Object> addToMockDoc(QRCode qrContent, String lat, String lon) {
//        Map<String, Object> data = new HashMap<>();
//        data.put("QRCode", qrContent);
//        data.put("Authors", new ArrayList<>());
//        data.put("Comments", new ArrayList<>());
//        if (lat != null && lon != null) {
//            GeoPoint geoPoint = new GeoPoint(Float.parseFloat(lat), Float.parseFloat(lon));
//            data.put("Location", geoPoint);
//        } else{
//            data.put("Location", null);
//        }
//        return data;
//    }
//
//
//    @Test
//    public void testQRLocation() {
//        // Mocking Everything related to firebase
//        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
//        CollectionReference collectionReference = db.collection("QRCodes");
//        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
//        Task<QuerySnapshot> taskMock =Mockito.mock(Task.class);
//        List<DocumentSnapshot> testData = new ArrayList<>();
//        MapsActivity mapsActivity = Mockito.mock(MapsActivity.class);
//        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
//
//
////        QRCode qr1 = new QRCode("qrtest1");
////        collectionReference.document(qr1.getHash()).set(addToMockDoc(qr1, "", ""));
////
////        QRCode qr2 = new QRCode("qrtest2");
////        collectionReference.document(qr2.getHash()).set(addToMockDoc(qr2, "", ""));
////
////        QRCode qr3 = new QRCode("qrtest3");
////        collectionReference.document(qr3.getHash()).set(addToMockDoc(qr3, "", ""));
////
////        QRCode qr4 = new QRCode("qrtest4");
////        collectionReference.document(qr4.getHash()).set(addToMockDoc(qr4, "", ""));
//
//        // Mocking the return values of the firebase methods
//        Mockito.when(taskMock.addOnSuccessListener(Mockito.any())).thenReturn(taskMock);
//        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
//        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
//        Mockito.when(collectionReference.get()).thenReturn(taskMock);
//        Mockito.when(querySnapshot.getDocuments()).thenReturn(testData);
//
//        QRLocation location = new QRLocation(db, mapsActivity);
//        for (QRCode qr: location.qrCodes) {
//            System.out.println(qr);
//        }
//
//    }
//
//}

@RunWith(MockitoJUnitRunner.class)
public class QRLocationUnitTest {

    @Mock
    private FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);

    @Mock
    private MapsActivity activity = Mockito.mock(MapsActivity.class);

    @Mock
    private CollectionReference collectionRef = Mockito.mock(CollectionReference.class);

    @Mock
    private Query query;

    @Mock
    private Task<QuerySnapshot> task = Mockito.mock(Task.class);

    @Mock
    private QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);

    @Mock
    private DocumentSnapshot documentSnapshot1 = Mockito.mock(DocumentSnapshot.class);

    @Mock
    private DocumentSnapshot documentSnapshot2 = Mockito.mock(DocumentSnapshot.class);

    @Mock
    private GeoPoint geoPoint;

    private List<DocumentSnapshot> documentList = Mockito.mock(List.class);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        documentList = new ArrayList<>();
        documentList.add(documentSnapshot1);
        documentList.add(documentSnapshot2);
    }

    @Test
    public void testGetQRCodes() {
        // Set up the mock objects
        when(db.collection("QRCodes")).thenReturn(collectionRef);
        when(collectionRef.get()).thenReturn(task);
        when(task.addOnSuccessListener(any())).thenReturn(task);
        when(task.addOnFailureListener(any())).thenReturn(task);
        when(querySnapshot.getDocuments()).thenReturn(documentList);
        when(documentSnapshot1.get("Location")).thenReturn(geoPoint);
        when(documentSnapshot1.getGeoPoint("Location")).thenReturn(geoPoint);
        when(documentSnapshot1.get("QRCode")).thenReturn(Collections.singletonMap("hash", "qrHash1"));
        when(documentSnapshot2.get("Location")).thenReturn(null);
        when(documentSnapshot2.getGeoPoint("Location")).thenReturn(null);
        when(documentSnapshot2.get("QRCode")).thenReturn(Collections.singletonMap("hash", "qrHash2"));

        // Execute the code to be tested
        ArrayList<QRCode> qrCodes = new ArrayList<>();
        QRLocation qrLocation = new QRLocation(db, activity);

        // Verify the results

        QRCode q1 = new QRCode("Manan");
        QRCode q2 = new QRCode("Kastuba");
        QRCode q3 = new QRCode("Mansoor");

        QRCode q4 = new QRCode(q1.getHash(), new LatLng(3434242.43,3434556.11));
        QRCode q5 = new QRCode(q2.getHash(), new LatLng(3434232.43,3433235.11));

        qrCodes.add(q4);
        qrCodes.add(q5);
        qrCodes.add(q3);

        qrLocation.setArray(qrCodes);

        ArrayList<QRCode> testQRCodes = new ArrayList<QRCode>();
        testQRCodes.add(q4);
        testQRCodes.add(q5);

        assertEquals(testQRCodes,qrLocation.getLocatedQRArray());

        testQRCodes.add(q3);
        assertNotSame(testQRCodes, qrLocation.getLocatedQRArray());

    }
}
