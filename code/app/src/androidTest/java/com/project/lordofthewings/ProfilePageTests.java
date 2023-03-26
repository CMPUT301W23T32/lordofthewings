package com.project.lordofthewings;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.lordofthewings.Models.QRcode.QRCode;
import com.project.lordofthewings.Views.EditProfileFragment;
import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.ProfilePage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.robotium.solo.Solo;
import com.squareup.picasso.Picasso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class ProfilePageTests {
    ArrayList<String> qrCodes_test;
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
        editor.putString("username", "bobtest");
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

    @Test
    public void checkIfHomeReturns() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.clickOnView(solo.getView(R.id.backIcon));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    @Test
    public void checkEditButton() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.clickOnView(solo.getView(R.id.editIcon));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.enterText(0, "Bob");
        solo.enterText(1, "Lal");
        solo.searchText("Bob");
        solo.searchText("Lal");
        solo.clickOnButton(1);
        solo.searchText("Bob");
        solo.searchText("Lal");
        solo.clickOnView(solo.getView(R.id.editIcon));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.enterText(0, "bobtest");
        solo.enterText(1, "bobtest");
        solo.clickOnButton(1);
    }

    @Test
    public void checkEditCancel() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.clickOnView(solo.getView(R.id.editIcon));
        solo.clickOnButton(0);
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
    }

    @Test
    public void checkProfileData() {
        final Integer[] rank_value = {0};
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.searchText("318");
        solo.searchText("6");
        solo.searchText("@bobtest");
        solo.searchText("LuminOnyxLuminCrimVoltLumin");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document("bobtest");
        CollectionReference collectionReference = db.collection("QRCodes");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    qrCodes_test = new ArrayList<>(task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        qrCodes_test.add(document.getId());
                    }
                    if (qrCodes_test != null) {
                        ArrayList<String> qrcodes_test_2 = new ArrayList<>();
                        Integer count = qrCodes_test.size();
                        for (int i = 0; i < count; i++) {
                            Integer key = 0;
                            for (int j = 1; j < qrCodes_test.size(); j++) {
                                if (new QRCode(qrCodes_test.get(key), 0).getQRScore() < new QRCode(qrCodes_test.get(j), 0).getQRScore())
                                    key = j;
                            }
                            qrcodes_test_2.add(qrCodes_test.get(key));
                            qrCodes_test.remove(qrCodes_test.get(key));
                        }
                        for (int i = 0; i < count; i++) {
                            qrCodes_test.add(qrcodes_test_2.get(i));
                        }
                    }
                    Log.d("DATA: ", qrCodes_test.get(1));
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.e("data", document.get("QRCodes").toString());
                                    ArrayList<Map<String, Object>> qrCodes = (ArrayList<Map<String, Object>>) document.get("QRCodes");
                                    ArrayList<Map<String, Object>> qrCodes2 = new ArrayList<>();
                                    if (qrCodes != null) {
                                        Integer count = qrCodes.size();
                                        for (int i = 0; i < count; i++) {
                                            Integer key = 0;
                                            for (int j = 1; j < qrCodes.size(); j++) {
                                                if (new QRCode(qrCodes.get(key).get("hash").toString(), 0).getQRScore() < new QRCode(qrCodes.get(j).get("hash").toString(), 0).getQRScore())
                                                    key = j;
                                            }
                                            qrCodes2.add(qrCodes.get(key));
                                            qrCodes.remove(qrCodes.get(key));
                                        }
                                        for (int i = 0; i < count; i++) {
                                            qrCodes.add(qrCodes2.get(i));
                                        }
                                        rank_value[0] = (Integer) (qrCodes_test.indexOf(qrCodes.get(0).get("hash"))) + 1;
                                        assertEquals(String.valueOf(rank_value[0]), ((TextView) solo.getView(R.id.qr_ranking)).getText());
                                    }
                                } else {
                                    Log.d("No Doc", "No such document");
                                }
                            } else {
                                Log.d("Err", "get failed with ", task.getException());
                            }
                        }
                    });
                } else {
                    Log.d("error","Error getting documents: ");
                }
            }
        });
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
