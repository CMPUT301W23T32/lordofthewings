package com.project.lordofthewings;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
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
import com.project.lordofthewings.Views.MainActivity;
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
    SharedPreferences savedUsername = getInstrumentation().getTargetContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
    String username = savedUsername.getString("username", "");

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
        editor.putString("username", "Sauron");
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

    /**
     * Checks if returns back to homepage
     */
    @Test
    public void checkIfReturnsHome() {
        solo.clickOnView(solo.getView(R.id.backIcon2));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    /**
     * Check is goes to repected users profile on clicking on listview item
     */
    @Test
    public void checkIfGoesToUserProfile() {
        solo.clickInList(0);
        solo.waitForActivity("ProfilePage");
        solo.assertCurrentActivity("ProfilePage", ProfilePage.class);
    }

    /**
     * Checks if search bar works as intended
     */
    @Test
    public void checkIfSearchBarWorks() {
        solo.clickOnView(solo.getView(R.id.search_leaderboard));
        solo.enterText(0, "Sauron");
        solo.searchText("Sauron");
    }

    /**
     * Checks if the item that appeared on search is clickable or not
     */
    @Test
    public void checkIfClickableOnSearch() {
        solo.clickOnView(solo.getView(R.id.search_leaderboard));
        solo.enterText(0, "Sauron");
        solo.searchText("Sauron");
        solo.clickInList(0);
        solo.waitForActivity("ProfilePage");
        solo.assertCurrentActivity("ProfilePage", ProfilePage.class);
    }

    /**
     * Close activity after each test and log back to the user who was signed in before running the tests
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        SharedPreferences sh1 = rule.getActivity().getSharedPreferences("sharedPrefs", 0);
        SharedPreferences.Editor editor = sh1.edit();
        editor.putString("username", username);
        editor.apply();
        Intent mainIntent = new Intent(solo.getCurrentActivity(), MainActivity.class);
        solo.getCurrentActivity().startActivity(mainIntent);
        solo.finishOpenedActivities();
    }
}
