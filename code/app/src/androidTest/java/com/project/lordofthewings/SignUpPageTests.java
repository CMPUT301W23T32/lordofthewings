package com.project.lordofthewings;


import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.MapsActivity;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class SignUpPageTests{
    private Solo solo;

    SharedPreferences savedUsername = getInstrumentation().getTargetContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String username = savedUsername.getString("username", "");


    @Rule
    public ActivityTestRule<SignUpPage> rule =
            new ActivityTestRule<>(SignUpPage.class, true, true);


    @Before
    public void setUp() throws Exception {
        SharedPreferences sh = getInstrumentation().getTargetContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        solo = new Solo(getInstrumentation(),rule.getActivity());
        SharedPreferences.Editor editor = sh.edit();
        editor.clear();
        editor.apply();
        solo.getCurrentActivity().startActivity(new Intent(solo.getCurrentActivity(), SignUpPage.class));
        solo.waitForActivity("SignUpPage");
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
    }


    @Test
    public void checkEmptyFields() {
        solo.clickOnView(solo.getView(R.id.username));
        solo.enterText((android.widget.EditText) solo.getView(R.id.username), "Tester");
        solo.clickOnView(solo.getView(R.id.firstName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.firstName), "Bob");
        solo.clickOnView(solo.getView(R.id.lastName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.lastName), "Test");
        solo.clickOnButton(0);
        solo.waitForText("Please fill in all the fields!");
        assertFalse(checkIfUserExistsinDB("Tester"));
    }


    @Test
    public void checkUsernameTaken(){
        solo.clickOnView(solo.getView(R.id.username));
        assertTrue(checkIfUserExistsinDB("ntt"));
        solo.enterText((android.widget.EditText) solo.getView(R.id.username), "ntt");
        solo.clickOnView(solo.getView(R.id.firstName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.firstName), "Bob");
        solo.clickOnView(solo.getView(R.id.lastName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.lastName), "Test");
        solo.clickOnView(solo.getView(R.id.email));
        solo.enterText((android.widget.EditText) solo.getView(R.id.email), "ntt@gmail.com");
        solo.clickOnButton(0);
        solo.waitForText("Username already exists!");
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
    }



    @Test
    public void checkCorrectLogin(){
        solo.clickOnView(solo.getView(R.id.username));
        assertFalse(checkIfUserExistsinDB("UserTester"));
        solo.enterText((android.widget.EditText) solo.getView(R.id.username), "UserTester");
        solo.clickOnView(solo.getView(R.id.firstName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.firstName), "Bob");
        solo.clickOnView(solo.getView(R.id.lastName));
        solo.enterText((android.widget.EditText) solo.getView(R.id.lastName), "Test");
        solo.clickOnView(solo.getView(R.id.email));
        solo.enterText((android.widget.EditText) solo.getView(R.id.email), "UserTester@gmail.com");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        assertTrue(checkIfUserExistsinDB("UserTester"));
    }

    @After
public void tearDown() throws Exception {
        deleteFromFB("UserTester");
        SharedPreferences sh1 = rule.getActivity().getSharedPreferences("sharedPrefs", 0);
        SharedPreferences.Editor editor = sh1.edit();
        editor.putString("username", username);
        editor.apply();
        Intent mainIntent = new Intent(solo.getCurrentActivity(), MainActivity.class);
        solo.getCurrentActivity().startActivity(mainIntent);
        solo.finishOpenedActivities();
    }


    public void deleteFromFB(String user) {
        db.collection("Users").document(user).delete();
    }

    public boolean checkIfUserExistsinDB(String user) {
        Task<DocumentSnapshot> documentSnapshotTask = db.collection("Users").document(user).get();
        DocumentSnapshot documentSnapshot = null;

        try {
            documentSnapshot = Tasks.await(documentSnapshotTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            return true;
        } else {
            return false;
        }
    }








}
