package com.project.lordofthewings;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.LeaderBoardPage;
import com.project.lordofthewings.Views.ProfilePage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class LeaderBoardTests {
    private Solo solo;
    ArrayList<Map<String, Object>> users_array;

    @Rule
    public ActivityTestRule<SignUpPage> rule =
            new ActivityTestRule<>(SignUpPage.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        SharedPreferences sh = rule.getActivity().getSharedPreferences("sharedPrefs", 0);
        SharedPreferences.Editor editor = sh.edit();
        editor.putString("username", "bobtest");
        editor.apply();
        Intent LeaderBoardPageIntent = new Intent(solo.getCurrentActivity(), LeaderBoardPage.class);
        solo.getCurrentActivity().startActivity(LeaderBoardPageIntent);
        solo.waitForActivity("Leaderboard");
        solo.assertCurrentActivity("Leaderboard", LeaderBoardPage.class);
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkIfReturnsHome() {
        solo.clickOnView(solo.getView(R.id.backIcon2));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    @Test
    public void checkIfGoesToUserProfile() {
        solo.clickInList(0);
        solo.waitForActivity("ProfilePage");
        solo.assertCurrentActivity("ProfilePage", ProfilePage.class);
    }

    @Test
    public void checkIfSearchBarWorks() {
        solo.clickOnView(solo.getView(R.id.search_leaderboard));
        solo.enterText(0, "bobtest");
        solo.searchText("bobtest");
    }

    @Test
    public void checkIfClickableOnSearch() {
        solo.clickOnView(solo.getView(R.id.search_leaderboard));
        solo.enterText(0, "bobtest");
        solo.searchText("bobtest");
        solo.clickInList(0);
        solo.waitForActivity("ProfilePage");
        solo.assertCurrentActivity("ProfilePage", ProfilePage.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
