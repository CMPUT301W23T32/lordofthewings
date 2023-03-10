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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.digest.DigestUtils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();

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
     * Checks if the Wallet Activity's back button works
     */
    @Test
    public void checkWalletBackButton() {
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        solo.clickOnView(solo.getView(R.id.walletButton));
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("Wrong Activity", WalletPage.class);
        solo.clickOnView(solo.getView(R.id.backIcon));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
    }

    /**
     * Checks if QR codes are present in wallet after user adds a qr code in the app
     */
    @Test
    public void checkaddnewqr() {
        QRCode QR1 = new QRCode("testing");

        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
//        Context context = solo.getCurrentActivity();
//        SharedPreferences sh = context.getSharedPreferences(
//                "sharedPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sh.edit();
//        editor.clear();
//        editor.putString("username", "test4");
//        editor.apply();
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        addintofbase(QR1);
        solo.clickOnView(solo.getView(R.id.walletButton));
        solo.waitForActivity("WalletPage");
        solo.assertCurrentActivity("Wrong Activity", WalletPage.class);
        solo.sleep(20000);
        ListView list_view = (ListView) solo.getView(R.id.qrCodeListView);
        assertEquals(1, list_view.getCount());
    }

    /**
     * Add the QRcode along with details into firebase
     * @param qrcode_name
     */
    public void addintofbase(QRCode qrcode_name) {
        db.collection("Users").document("mktest").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) documentSnapshot.get("QRCodes");
                ArrayList<QRCode> qrArray = new ArrayList<>();
                for (int i = 0; i < qrCodes.size(); i++) {
                    Map<String, Object> qrObject = (Map<String, Object>) qrCodes.get(i);
                    QRCode qrCode = new QRCode(qrObject.get("hash").toString(), 1);
                    qrArray.add(qrCode);
                }
                qrArray.add(qrcode_name);
                documentSnapshot.getReference().update("QRCodes", qrArray);
            }
        });
    }

    /**
     * deletes the test user from the firestore database
     */
    public void deleteTestUser(){
        db.collection("Users").document("mktest")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /**
     * Close activity after each test, deletes the test user from firestore after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        deleteTestUser();
    }
}
