package com.project.lordofthewings;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.QRCodePage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.WalletPage;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class QRCodePageTests {
    private Solo solo;
    SharedPreferences savedUsername = getInstrumentation().getTargetContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
    String username = savedUsername.getString("username", "");
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
        editor.putString("username", "Sauron");
        editor.apply();
        Intent walletIntent = new Intent(solo.getCurrentActivity(), WalletPage.class);
        solo.getCurrentActivity().startActivity(walletIntent);
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);


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
     * Checks if returns back to Wallet Page from QRCodePage
     */
    @Test
    public void checkReturntoWallet() {
        solo.clickInList(1);
        solo.waitForActivity("QRCodePage");
        solo.assertCurrentActivity("QRCodePage", QRCodePage.class);
        solo.clickOnView(solo.getView(R.id.backIcon));
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);
    }

    /**
     * Checks if the comments are being displayed or not
     */
    @Test
    public void checkComments() {
        solo.clickInList(1);
        solo.waitForActivity("QRCodePage");
        solo.assertCurrentActivity("QRCodePage", QRCodePage.class);
        solo.clickOnText("Comments");
        solo.searchText("This is a comment.");
    }

    /**
     * Checks if the scanned by names are being displayed correctly or not
     */
    @Test
    public void checkScannedBy() {
        solo.clickInList(1);
        solo.waitForActivity("QRCodePage");
        solo.assertCurrentActivity("QRCodePage", QRCodePage.class);
        solo.clickOnText("Scanned By");
        solo.searchText("Sauron");
    }

    /**
     * Checks if the Location Image tab is clickable or not
     */
    @Test
    public void checkLocationInfoClickable() {
        solo.clickInList(1);
        solo.waitForActivity("QRCodePage");
        solo.assertCurrentActivity("QRCodePage", QRCodePage.class);
        solo.clickOnText("Location Image");
    }

    /**
     * Checks if the QRCodeInfo tab is clickable or not
     */
    @Test
    public void checkQRCodeInfoClickable() {
        solo.clickInList(1);
        solo.waitForActivity("QRCodePage");
        solo.assertCurrentActivity("QRCodePage", QRCodePage.class);
        solo.clickOnText("Location Image");
        solo.sleep(2000);
        solo.clickOnText("QR Code Info");
        solo.sleep(2000);
    }

    /**
     * Checks if the QRCode data displayed is correct or not
     */
    @Test
    public void checkQRCodeData() {
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
        solo.searchText("LuminOnyxLuminCrimVoltLumin");
        solo.searchText("94");
        solo.clickOnText("LuminOnyxLuminCrimVoltLumin");
        solo.clickOnView(solo.getView(R.id.deleteIcon));
        solo.clickOnButton(1);
    }

    /**
     * Checks if the delete button works as intended
     */
    @Test
    public void checkDeleteButton() {
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
        solo.clickOnText("LuminOnyxLuminCrimVoltLumin");
        solo.clickOnView(solo.getView(R.id.deleteIcon));
        solo.sleep(2000);
        solo.clickOnButton(1);
        solo.sleep(2000);
        assertFalse(solo.searchText("LuminOnyxLuminCrimVoltLumin"));
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
