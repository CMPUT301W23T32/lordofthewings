package com.project.lordofthewings.Models.QRLocation;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.QRcode.QRCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  A class which deals with all the QRCodes in existence
 */
public class QRLocation {
    ArrayList<QRCode> qrCodes;
    QRCodeCallback callback;
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();

    public QRLocation(QRCodeCallback callback){
        this.qrCodes = new ArrayList<>();
        this.callback = callback;
        // get QRCodes from Firebase
            db.collection("QRCodes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        LatLng pos = new LatLng(snapshot.getGeoPoint("location").getLatitude(), snapshot.getGeoPoint("location").getLongitude());
                        QRCode qrCode = new QRCode(snapshot.getString("hash"), pos);
                        System.out.println("1st check: " + qrCodes);
                        qrCodes.add(qrCode);
                        System.out.println("2nd check: " + qrCodes);
                    }
                    callback.onQrCodesRecieved();
                }
            });

    }

    // fetches QR codes that are to be located on the map
    public ArrayList<QRCode> getLocatedQRArray(){

        ArrayList<QRCode> locatedQrs = new ArrayList<>();
        for (QRCode code: this.qrCodes) {
            if (code.getLocation() != null){
                locatedQrs.add(code);
            }
        }


        this.db.terminate();
        return locatedQrs;
    }

}
