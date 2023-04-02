package com.project.lordofthewings;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
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
public class CaptureActivityTests {
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
     * Checks if Activity returns to Homepage from CaptureActivity
     */
    @Test
    public void checkIfReturnsToHomeOnCancel() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        Intent intent = new Intent(solo.getCurrentActivity(), QRCodeScan.class);
        // go to intent
        intent.putExtra("qr_code", "qr_code");
        solo.getCurrentActivity().startActivity(intent);
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("QRCodeScan", QRCodeScan.class);
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.cancel_button));
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    /**
     * Checks if Activity returns to Homepage from CaptureActivity
     */
    @Test
    public void checkIfReturnsToHomeOnScan() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        Intent intent = new Intent(solo.getCurrentActivity(), QRCodeScan.class);
        // go to intent
        intent.putExtra("qr_code", "qr_code");
        solo.getCurrentActivity().startActivity(intent);
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("QRCodeScan", QRCodeScan.class);
        // Button click give a result data
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.save_button));
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    /**
     * Checks if the QR code name is correct
     */
    @Test
    public void checkCorrectQRCodeName() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        Intent intent = new Intent(solo.getCurrentActivity(), QRCodeScan.class);
        // go to intent
        intent.putExtra("qr_code", "qr_code");
        solo.getCurrentActivity().startActivity(intent);
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("QRCodeScan", QRCodeScan.class);
        QRCode code = new QRCode("qr_code");
        // print the name of the qr code in java
        solo.waitForText("LuminOnyxLuminCrimVoltLumin");
        assertTrue(solo.searchText(code.getQRName()));
        //assertFalse(solo.searchText("LmiOnyLumiCrimoltumin"));
    }

    /**
     * Checks if the QR code score is correct
     */
    @Test
    public void checkCorrectQRCodeScore() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        Intent intent = new Intent(solo.getCurrentActivity(), QRCodeScan.class);
        // go to intent
        intent.putExtra("qr_code", "qr_code");
        solo.getCurrentActivity().startActivity(intent);
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("QRCodeScan", QRCodeScan.class);
        QRCode code = new QRCode("qr_code");
        solo.sleep(1000);
        assertTrue(solo.searchText(String.valueOf(code.getQRScore())));
    }

    @Test
    public void checkForDuplication() {
        //solo.clickOnView(solo.getView(R.id.scanButton));
        Intent intent = new Intent(solo.getCurrentActivity(), QRCodeScan.class);
        // go to intent
        intent.putExtra("qr_code", "qr_code");
        solo.getCurrentActivity().startActivity(intent);
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("QRCodeScan", QRCodeScan.class);
        // Button click give a result data
        solo.sleep(5000);
        solo.clickOnView(solo.getView(R.id.save_button));
        solo.assertCurrentActivity("HomePage", HomePage.class);
        solo.clickOnView(solo.getView(R.id.walletButton));
        solo.clickOnText("LuminOnyxLuminCrimVoltLumin");
        solo.clickOnView(solo.getView(R.id.deleteIcon));
        solo.clickOnButton(1);
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
