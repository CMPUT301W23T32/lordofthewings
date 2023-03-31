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
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.LeaderBoardPage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.MapsActivity;
import com.project.lordofthewings.Views.ProfilePage;
import com.project.lordofthewings.Views.QRCodePage;
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

import java.util.ArrayList;
import java.util.HashMap;

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
    public void testBackButtonReturnsToPreviousActivity() {
        solo.waitForActivity("MapsActivity");
        solo.assertCurrentActivity("MapsActivity", MapsActivity.class);
        solo.clickOnView(solo.getView(R.id.back_button_map));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    @Test
    public void testLocationSearch() {
        solo.sleep(4000);
        solo.clickOnView(solo.getView(R.id.search_text));
        solo.enterText((EditText) solo.getView(R.id.search_text), "University of Alberta");
        solo.pressSoftKeyboardSearchButton();
        solo.sleep(4000);
        solo.waitForText("University of Alberta", 1, 2000);
        solo.clickOnImage(0);
        solo.sleep(4000);


    }

    @Test
    public void testLocationSearchNotFoundMessage() {
        solo.sleep(4000);
        solo.clickOnView(solo.getView(R.id.search_text));
        solo.enterText((EditText) solo.getView(R.id.search_text), "RandomCountryDoesNotExist");
        solo.pressSoftKeyboardSearchButton();
        solo.waitForText("No location found", 1, 2000);

    }

    @Test
    public void testSwitchesToQRCodePage(){
        solo.sleep(4000);
        int heightOfDevice = Resources.getSystem().getDisplayMetrics().heightPixels;
        solo.drag(0, 0, heightOfDevice , heightOfDevice -= 1000000, 10);
        solo.sleep(4000);
        solo.clickOnText("all");
        solo.sleep(3000);
        // Get MapsActivity to access its variables and methods.
        MapsActivity activity = (MapsActivity) solo.getCurrentActivity();
        ArrayAdapter<HashMap<QRCode, Float>> list = activity.getList(); // Get the listview
        if (list.getCount()>0) {
            QRCode qrCode = (QRCode) (list.getItem(0)).keySet().toArray()[0];
            solo.clickInList(0);
            solo.waitForActivity("QRCodePage");
            solo.assertCurrentActivity("Wrong Activity", QRCodePage.class);

        }
    }

    @Test
    public void testSwitchesToQRCodePageCorrectQrInfo(){
        solo.sleep(4000);
        int heightOfDevice = Resources.getSystem().getDisplayMetrics().heightPixels;
        solo.drag(0, 0, heightOfDevice , heightOfDevice -= 1000000, 10);
        solo.sleep(5000);
        solo.clickOnText("all");
        solo.sleep(5000);
        // Get MapsActivity to access its variables and methods.
        MapsActivity activity = (MapsActivity) solo.getCurrentActivity();
        ArrayAdapter<HashMap<QRCode, Float>> list = activity.getList(); // Get the listview
        if (list.getCount()>0 && list != null) {
            QRCode qrCode = (QRCode) (list.getItem(0)).keySet().toArray()[0];
            String name = qrCode.getQRName();
            int score = qrCode.getQRScore();
            solo.clickInList(0);
            solo.waitForActivity("QRCodePage");
            solo.assertCurrentActivity("Wrong Activity", QRCodePage.class);
            solo.waitForText(name, 1, 2000);
            solo.waitForText(Integer.toString(score), 1, 2000);
        }
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













