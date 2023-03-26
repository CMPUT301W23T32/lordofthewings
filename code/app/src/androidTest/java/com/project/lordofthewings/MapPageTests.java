package com.project.lordofthewings;

import static android.content.ContentValues.TAG;
import static android.view.KeyEvent.KEYCODE_ENTER;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.LeaderBoardPage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.MapsActivity;
import com.project.lordofthewings.Views.ProfilePage;
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
public class MapPageTests {
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
        Intent mapIntent = new Intent(solo.getCurrentActivity(), MapsActivity.class);
        solo.getCurrentActivity().startActivity(mapIntent);
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("MapsActivity",MapsActivity.class);

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

    //In the real app due to the activity flow it will always return to homePage, due to how we have
    //written @Before we do not go to the homepage. (the back button just calls finish() not sending
    //intents)
    @Test
    public void backButtonReturnsToPreviousActivity() {
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("MapsActivity", MapsActivity.class);
        solo.clickOnView(solo.getView(R.id.back_button_map));
        solo.waitForActivity("SignUpPage");
        solo.assertCurrentActivity("SignUpPage", SignUpPage.class);
    }

    @Test
    public void TestLocationSearch() {
        solo.sleep(4000);
        solo.clickOnView(solo.getView(R.id.search_text));
        solo.enterText((EditText) solo.getView(R.id.search_text), "University of Alberta");
        solo.pressSoftKeyboardSearchButton();
        solo.waitForText("University of Alberta", 1, 2000);
        solo.clickOnImage(0);
        solo.sleep(4000);


    }

    @Test
    public void TestLocationSearchNotFoundMessage() {
        solo.sleep(4000);
        solo.clickOnView(solo.getView(R.id.search_text));
        solo.enterText((EditText) solo.getView(R.id.search_text), "RandomCountryDoesNotExist");
        solo.pressSoftKeyboardSearchButton();
        solo.waitForText("No location found", 1, 2000);



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













