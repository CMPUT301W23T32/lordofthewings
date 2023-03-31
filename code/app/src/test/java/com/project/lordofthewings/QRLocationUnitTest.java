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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class QRLocationUnitTest {

    @Test
    public void testQRLocation() {
        // Mocking Everything related to firebase
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Task<QuerySnapshot> taskMock =Mockito.mock(Task.class);
        OnSuccessListener<QuerySnapshot> listenerMock = Mockito.mock(OnSuccessListener.class);
        List<DocumentSnapshot> testData = new ArrayList<>();


        // Mocking the return values of the firebase methods
        Mockito.when(taskMock.addOnSuccessListener(Mockito.any())).thenReturn(taskMock);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
        Mockito.when(collectionReference.get()).thenReturn(taskMock);
        Mockito.when(querySnapshot.getDocuments()).thenReturn(testData);

    }

}
