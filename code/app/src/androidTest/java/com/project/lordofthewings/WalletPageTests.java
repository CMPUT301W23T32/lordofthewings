package com.project.lordofthewings;

import static android.content.ContentValues.TAG;
import static android.provider.Settings.System.getString;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.StartUpPages.LogInPage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.StartUpPages.StartUpPage;
import com.project.lordofthewings.Views.WalletPage;
import com.robotium.solo.Solo;

import org.apache.commons.codec.binary.Hex;
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
import java.util.Map;

/**
 * Test class for activity flow. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class WalletPageTests {
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
     * CHecks if the page switches from WalletPage to HomePage
     */
    @Test
    public void checkIfReturnsHome() {
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);
        solo.clickOnView(solo.getView(R.id.backIcon));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    /**
     * Checks if the app displays the correct amount of QR Codes in the listview
     */
    @Test
    public void checkNumberOfQRListView() {
        ListView listView = (ListView) solo.getView(R.id.qrCodeListView);
        solo.sleep(5000);
        assertEquals(6, listView.getCount());
    }

    /**
     * Checks if the app displays the correct amount of QR Codes in the QRCodes textview
     */
    @Test
    public void checkNumberOfQRTextView() {
        TextView textView = (TextView) solo.getView(R.id.qrcodeCount);
        solo.sleep(5000);
        assertEquals("6", textView.getText());
    }

    /**
     * Checks if switches to comeraview on clicking scan a new QR code
     */
    @Test
    public void checkScanANewQr() {
        solo.clickOnView(solo.getView(R.id.scanButton));
        solo.waitForActivity("CaptureActivity");
        solo.assertCurrentActivity("CaptureActivity", CaptureActivity.class);
    }

    /**
     * Checks if the total points matches the displayed total points
     */
    @Test
    public void checkQrTotalScore() {
        TextView textView = (TextView) solo.getView(R.id.points);
        solo.sleep(5000);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference_users = db.collection("Users");
        collectionReference_users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    users_array = new ArrayList<>(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        users_array.add(document.getData());
                    }
                    ArrayList<String> users_usernames = new ArrayList<>();

                    if (users_array != null) {
                        ArrayList<Map<String, Object>> users_array_2 = new ArrayList<>();
                        Integer count_users = users_array.size();
                        for (int i = 0; i < count_users; i++) {
                            Integer key = 0;
                            for (int j = 1; j < users_array.size(); j++) {
                                if ((int) (long) users_array.get(key).get("Score") < (int) (long) users_array.get(j).get("Score"))
                                    key = j;
                            }
                            users_array_2.add(users_array.get(key));
                            users_array.remove(users_array.get(key));
                        }
                        for (int i = 0; i < count_users; i++) {
                            users_array.add(users_array_2.get(i));
                            users_usernames.add((String) users_array_2.get(i).get("username"));
                        }

                    } else {
                        Log.d("Error", "No data present");
                    }
                    assertEquals(users_array.get(users_usernames.indexOf("bobtest")).get("Score") + " Points", textView.getText());
                } else {
                    Log.d("Error: ", "Couldn't get users");
                }
            }
        });
    }

    /**
     * Checks if returns back to wallet trying to go back from Capture Activity
     */
    @Test
    public void checkIfReturnWallet() {
        solo.clickOnView(solo.getView(R.id.scanButton));
        solo.waitForActivity("CaptureActivity");
        solo.assertCurrentActivity("CaptureActivity", CaptureActivity.class);
        solo.goBack();
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("WalletPage", WalletPage.class);
    }

    /**
     * Checks if the ascending order button works
     */
    @Test
    public void checkIfAscendingWorks() {
        solo.clickOnView(solo.getView(R.id.ascendingChip));
        solo.sleep(10000);
        ListView listView = (ListView) solo.getView(R.id.qrCodeListView);
        QRCode qrCode_top = (QRCode) listView.getAdapter().getItem(0);
        QRCode qrCode_end = (QRCode) listView.getAdapter().getItem(5);
        assertEquals("CrimKronVoltNyxNebuZax", qrCode_top.getQRName());
        assertEquals("LuminOnyxLuminCrimVoltLumin", qrCode_end.getQRName());
    }

    /**
     * Checks if the descending order button works
     */
    @Test
    public void checkIfDescendingWorks() {
        solo.clickOnView(solo.getView(R.id.descendingChip));
        solo.sleep(10000);
        ListView listView = (ListView) solo.getView(R.id.qrCodeListView);
        QRCode qrCode_top = (QRCode) listView.getAdapter().getItem(0);
        QRCode qrCode_end = (QRCode) listView.getAdapter().getItem(5);
        assertEquals("LuminOnyxLuminCrimVoltLumin", qrCode_top.getQRName());
        assertEquals("CrimKronVoltNyxNebuZax", qrCode_end.getQRName());
    }

    /**
     * Checks if the default order button works
     */
    @Test
    public void checkIfDefaultWorks() {
        solo.clickOnView(solo.getView(R.id.defaultChip));
        solo.sleep(10000);
        ListView listView = (ListView) solo.getView(R.id.qrCodeListView);
        QRCode qrCode_top = (QRCode) listView.getAdapter().getItem(0);
        QRCode qrCode_end = (QRCode) listView.getAdapter().getItem(5);
        assertEquals("KinetCrimKronHexVexZax", qrCode_top.getQRName());
        assertEquals("LuminOnyxLuminCrimVoltLumin", qrCode_end.getQRName());
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
