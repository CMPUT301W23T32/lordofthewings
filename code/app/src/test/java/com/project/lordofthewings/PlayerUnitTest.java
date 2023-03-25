package com.project.lordofthewings;



import static org.mockito.Mockito.mock;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Models.Player.Player;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PlayerUnitTest {
    @Test
    public void testPlayer() throws Exception {
        FirebaseFirestore db = mock(FirebaseFirestore.class);
        // Create a mock instance of DocumentReference
        DocumentReference documentRef = mock(DocumentReference.class);

        // Create a mock instance of DocumentSnapshot
        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
        Player p = new Player("test","test@test", "testF","testL", db);
    }
}
