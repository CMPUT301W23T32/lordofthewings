package com.project.lordofthewings;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.LeaderBoardPage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.MapsActivity;
import com.project.lordofthewings.Views.ProfilePage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.WalletPage;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test class for activity flow. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class HomePageTests {
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
        Intent homeIntent = new Intent(solo.getCurrentActivity(), HomePage.class);
        solo.getCurrentActivity().startActivity(homeIntent);
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
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
     * Checks if goes to wallet page on clicking the wallet button
     */
    @Test
    public void checkWalletButton() {
        solo.clickOnView(solo.getView(R.id.walletButton));
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);
    }

    /**
     * Checks if goes to map page on clicking map button
     */
    @Test
    public void checkMapButton() {
        solo.clickOnView(solo.getView(R.id.mapButton));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("MapsActivity", MapsActivity.class);
    }

    /**
     * Checks if goes to profile page on clicking profile button
     */
    @Test
    public void checkProfileButton() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
    }

    /**
     * Checks if goes to leaderboard page on clicking leaderboard button
     */
    @Test
    public void checkLeaderBoardButton() {
        solo.clickOnView(solo.getView(R.id.leaderboardButton));
        solo.waitForActivity("LeaderBoardPage");
        solo.assertCurrentActivity("LeaderBoardPage", LeaderBoardPage.class);
    }

    /**
     * Checks if switches to scanner on clicking scanner button
     */
    @Test
    public void checkScanButton() {
        solo.clickOnView(solo.getView(R.id.scanButton));
        solo.waitForActivity("CaptureActivity");
        solo.assertCurrentActivity("CaptureActivity", CaptureActivity.class);
    }

    /**
     * Checks if the username displayed is the right username
     */
    @Test
    public void checkUsername(){
        solo.searchText("Sauron");
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
