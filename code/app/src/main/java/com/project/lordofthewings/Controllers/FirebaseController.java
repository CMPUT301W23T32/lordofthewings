package com.project.lordofthewings.Controllers;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A singleton class that creates a single instance of the Firebase Firestore database
 */
public class FirebaseController {
    private static boolean IS_INITIALIZED = false;
    private FirebaseFirestore db;


    /**
     * Constructor for a single instance of the Firebase Firestore database
     */
    public FirebaseController() {
        if (!IS_INITIALIZED) {
            db = FirebaseFirestore.getInstance();
            IS_INITIALIZED = true;
        }
    }

    /**
     * Getter for the Firebase Firestore database
     * @return the Firebase Firestore database
     */
    public FirebaseFirestore getDb() {
        if (db == null) {
            this.db = FirebaseFirestore.getInstance();
        }
        return this.db;
    }
}
