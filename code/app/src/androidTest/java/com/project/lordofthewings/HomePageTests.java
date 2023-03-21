package com.project.lordofthewings;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.MapsActivity;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.StartUpPages.StartUpPage;
import com.project.lordofthewings.Views.WalletPage;
import com.robotium.solo.Solo;

import org.jetbrains.annotations.TestOnly;
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
        editor.putString("username", "ntt");
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


    @Test
    public void checkWalletButton() {
        solo.clickOnView(solo.getView(R.id.walletButton));
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);
    }

    @Test
    public void checkMapButton() {
        solo.clickOnView(solo.getView(R.id.mapButton));
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("MapsActivity", MapsActivity.class);
    }

//    @Test
//    public void checkUserProfileButton() {
//        solo.clickOnView(solo.getView(R.id.profileButton));
//        solo.waitForActivity("UserProfilePage");
//        solo.assertCurrentActivity("UserProfilePage", .class);
//    }

//    @Test
//    public void checkUserLeaderBoardButton() {
//        solo.clickOnView(solo.getView(R.id.profileButton));
//        solo.waitForActivity("UserProfilePage");
//        solo.assertCurrentActivity("UserProfilePage", .class);
//    }

    @Test
    public void checkUsername(){
        solo.searchText("ntt");
    }

    /**
     * Close activity after each test, deletes the test user from firestore after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
