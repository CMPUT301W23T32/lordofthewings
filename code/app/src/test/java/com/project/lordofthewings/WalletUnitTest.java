package com.project.lordofthewings;


import static org.junit.jupiter.api.Assertions.assertFalse;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.Futures;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Models.Wallet.Wallet;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class WalletUnitTest {

    /**
     * Test to check if the addQR method works
     */
    @Test
    public void testAddQR(){

        // Making a qrcode
        QRCode qr = new QRCode("testQR");

        // Mocking Everything related to firebase
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Task<DocumentSnapshot> taskMock = Mockito.mock(Task.class);
        OnCompleteListener<DocumentSnapshot> listenerMock = Mockito.mock(OnCompleteListener.class);

        // Mocking the return values of the firebase methods
        Mockito.when(taskMock.addOnCompleteListener(Mockito.any())).thenReturn(taskMock);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(db.collection("Users")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document("test1")).thenReturn(documentReference);
        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document(qr.getHash())).thenReturn(documentReference);
        Mockito.when(documentReference.update("QRList", new ArrayList<>())).thenReturn(null);
        Mockito.when(documentReference.get()).thenReturn(taskMock);

        // Creating a wallet
        Wallet w = new Wallet("test1", new ArrayList<>(), 0, db);

        // Testing the addQRCode method
        assert(w.getQrCodes().size() == 0);
        w.addQRCode(qr, "1222332", "1212133");
        assert(w.getQrCodes().size() == 1);
        assertFalse(w.getQrCodes().size() == 0);

    }

    /**
     * Test to check if the removeQR method works
     */
    @Test
    public void testDeleteQRCode(){
        // Making a qrcode
        QRCode qr = new QRCode("testQR");

        // Mocking Everything related to firebase
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Task<DocumentSnapshot> taskMock = Mockito.mock(Task.class);
        OnCompleteListener<DocumentSnapshot> listenerMock = Mockito.mock(OnCompleteListener.class);

        // Mocking the return values of the firebase methods
        Mockito.when(taskMock.addOnCompleteListener(Mockito.any())).thenReturn(taskMock);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(db.collection("Users")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document("test1")).thenReturn(documentReference);
        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document(qr.getHash())).thenReturn(documentReference);
        Mockito.when(documentReference.update("QRList", new ArrayList<>())).thenReturn(null);
        Mockito.when(documentReference.get()).thenReturn(taskMock);

        // Creating a wallet
        Wallet w = new Wallet("test1", new ArrayList<>(), 0, db);

        // Testing the DeleteQRCode method
        assert(w.getQrCodes().size() == 0);
        w.addQRCode(qr, "1222332", "1212133");
        assert(w.getQrCodes().size() == 1);
        w.deleteQRCode(qr);
        assert(w.getQrCodes().size() == 0);
    }

    /**
     * Test to check score update of qrcode
     */
    @Test
    public void testUpdateWalletScore(){
        // Making a qrcode
        QRCode qr = new QRCode("testQR");

        // Mocking Everything related to firebase
        FirebaseFirestore db = Mockito.mock(FirebaseFirestore.class);
        CollectionReference collectionReference = Mockito.mock(CollectionReference.class);
        DocumentReference documentReference = Mockito.mock(DocumentReference.class);
        DocumentSnapshot documentSnapshot = Mockito.mock(DocumentSnapshot.class);
        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        Task<DocumentSnapshot> taskMock = Mockito.mock(Task.class);
        OnCompleteListener<DocumentSnapshot> listenerMock = Mockito.mock(OnCompleteListener.class);

        // Mocking the return values of the firebase methods
        Mockito.when(taskMock.addOnCompleteListener(Mockito.any())).thenReturn(taskMock);
        Mockito.when(taskMock.isSuccessful()).thenReturn(true);
        Mockito.when(db.collection("Users")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document("test1")).thenReturn(documentReference);
        Mockito.when(db.collection("QRCodes")).thenReturn(collectionReference);
        Mockito.when(collectionReference.document(qr.getHash())).thenReturn(documentReference);
        Mockito.when(documentReference.update("QRList", new ArrayList<>())).thenReturn(null);
        Mockito.when(documentReference.get()).thenReturn(taskMock);

        // Creating a wallet
        Wallet w = new Wallet("test1", new ArrayList<>(), 0, db);

        // Testing the updateWalletScore method
        assert(w.getScore() == 0);
        w.addQRCode(qr, "1222332", "1212133");
        assert(w.getScore() != 0);
        assert(w.getScore()== 42);

        w.addQRCode(qr, "1222332", "1212133");
        assert(w.getScore() != 0);
        assert(w.getScore()== 84);

        w.deleteQRCode(qr);
        assert(w.getScore() != 0);
        assert(w.getScore() == 42);

        w.deleteQRCode(qr);
        assert(w.getScore() == 0);
    }
}
