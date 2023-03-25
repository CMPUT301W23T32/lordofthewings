package com.project.lordofthewings;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class DatabaseInteractor {
    private static User theUser;
    private FirebaseFirestore db;
    private DocumentReference userData;
    public DatabaseInteractor(FirebaseFirestore firestore) {
        db = firestore;
        userData = db.collection("users").document("test1");
    }
}
