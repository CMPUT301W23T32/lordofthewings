package com.project.lordofthewings;



import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Models.Player.Player;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class PlayerUnitTest {
    @Test
    public void testPlayerFirstName() throws Exception {
//        FirebaseFirestore db = mock(FirebaseFirestore.class);
//        // Create a mock instance of DocumentReference
//        DocumentReference documentRef = mock(DocumentReference.class);
//
//        // Create a mock instance of DocumentSnapshot
//        DocumentSnapshot documentSnapshot = mock(DocumentSnapshot.class);
//        when(documentSnapshot.exists()).thenReturn(true);
//        when(documentSnapshot.getData()).thenReturn(new HashMap<String, Object>() {{
//            put("email", "test@test");
//            put("firstName", "testF");
//            put("lastName", "testL");
//        }});
        Player p = new Player("test1","test@test", "testF","testL");
    }
}
