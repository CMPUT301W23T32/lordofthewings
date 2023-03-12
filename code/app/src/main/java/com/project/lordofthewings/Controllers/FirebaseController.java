package com.project.lordofthewings.Controllers;

import com.google.firebase.firestore.FirebaseFirestore;

/*
    * This class is used to create a single instance of the Firebase Firestore database
 */
public class FirebaseController {
    private static boolean IS_INITIALIZED = false;
    private FirebaseFirestore db;
    /*
        * This constructor creates a single instance of the Firebase Firestore database
     */
    public FirebaseController() {
        if (!IS_INITIALIZED) {
            db = FirebaseFirestore.getInstance();
            IS_INITIALIZED = true;
        }
    }
    /*
        * This method returns the single instance of the Firebase Firestore database
        * @return db
     */
    public FirebaseFirestore getDb() {
        if (db == null) {
            this.db = FirebaseFirestore.getInstance();
        }
        return this.db;
    }
}
