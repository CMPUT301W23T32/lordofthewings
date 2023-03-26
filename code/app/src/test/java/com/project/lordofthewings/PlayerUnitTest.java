package com.project.lordofthewings;



import static org.junit.jupiter.api.Assertions.assertTrue;
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
        Player p = new Player("test1","test@test", "testF","testL");
        assertTrue(p.getFirstName().equals("testF"));
    }
    @Test
    public void testPlayerLastName() throws Exception {
        Player p = new Player("test1","test@test", "testF","testL");
        assertTrue(p.getLastName().equals("testL"));
    }
    @Test
    public void testPlayerEmail() throws Exception {
        Player p = new Player("test1","test@test", "testF","testL");
        assertTrue(p.getEmail().equals("test@test"));
    }
    @Test
    public void testPlayerUsername() throws Exception {
        Player p = new Player("test1","test@test", "testF","testL");
        assertTrue(p.getUserName().equals("test1"));
    }
}
