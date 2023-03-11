package com.project.lordofthewings;

import static android.content.ContentValues.TAG;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lordofthewings.Controllers.FirebaseController;
import com.project.lordofthewings.Views.CameraPages.QRCodeScan;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
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
public class StartUpActivityTests {
    private Solo solo;
    FirebaseController fbController = new FirebaseController();
    FirebaseFirestore db = fbController.getDb();

    @Rule
    public ActivityTestRule<SignUpPage> rule =
            new ActivityTestRule<>(SignUpPage.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkIfSignUpSwitchesToHomepage() {
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

    }

    @Test
    public void checkIfSwitchesToQRCodeScan() {
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        solo.clickOnView(solo.getView(R.id.scanButton));
        solo.waitForActivity("QRCodeScan");
        solo.assertCurrentActivity("Wrong Activity", QRCodeScan.class);
        
    }



    @Test
    public void checkIfHomePageDisplaysCorrectUser() {
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        assertTrue(solo.waitForText("mktest", 1, 2000));

    }

    @Test
    public void checkIfLogOutWorks() {
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);
        solo.clickOnView(solo.getView(R.id.settingsButton));
        solo.clickOnMenuItem("Logout");
        solo.waitForActivity("SignUpPage");
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
    }
    @Test
    public void checkIfSwitchesToWallet() {
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

    }
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
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        deleteTestUser();

    }

}
