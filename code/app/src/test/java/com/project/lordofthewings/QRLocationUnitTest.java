package com.project.lordofthewings;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Models.QRcode.QRCode;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRLocationUnitTest {

    public void addToMockDoc(String qrContent, String lat, String lon, DocumentSnapshot documentSnapshot) {
        Map<String, Object> data = new HashMap<>();
        data.put("QRCode", qrContent);
        data.put("Authors", new ArrayList<>());
        data.put("Comments", new ArrayList<>());
        if (lat != null && lon != null) {
            GeoPoint geoPoint = new GeoPoint(Float.parseFloat(lat), Float.parseFloat(lon));
            data.put("Location", geoPoint);
        } else{
            data.put("Location", null);
        }
        documentSnapshot.getData().putAll(data);
    }


    @Test
    public void testQRLocation() {
        // Mocking Everything related to firebase
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Task<QuerySnapshot> taskMock =Mockito.mock(Task.class);
        OnSuccessListener<QuerySnapshot> listenerMock = Mockito.mock(OnSuccessListener.class);
        List<DocumentSnapshot> testData = new ArrayList<>();

        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        documentSnapshot.getData().put("location", new GeoPoint(0,0));

        QRCode qr = new QRCode("testQR");
        List<String> authors = new ArrayList<>();
        List<String> comments = new ArrayList<>();
        String lat = "0";
        String lon = "0";

        float latInt = (float) (Float.parseFloat(lat));
        float lonInt = (float) (Float.parseFloat(lon));
        if (lat != null && lon != null) {
            GeoPoint geoPoint = new GeoPoint(latInt, lonInt);
            data.put("Location", geoPoint);

        } else{
            data.put("Location", null);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("QRCode", qr);
        data.put("Authors", authors);
        data.put("Comments", comments);

        db.collection("QRCodes").document(qr.getHash())
                .set(data);

        // Mocking the return values of the firebase methods
        Mockito.when(taskMock.addOnSuccessListener(Mockito.any())).thenReturn(taskMock);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
        Mockito.when(collectionReference.get()).thenReturn(taskMock);
        Mockito.when(querySnapshot.getDocuments()).thenReturn(testData);
        Moc

    }

}
