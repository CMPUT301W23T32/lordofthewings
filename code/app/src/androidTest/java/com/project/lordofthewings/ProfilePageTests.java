package com.project.lordofthewings;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.project.lordofthewings.Views.MainActivity;
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
    ArrayList<Map<String, Object>> users_array;
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
     * Checks if returns to home successfully
     */
    @Test
    public void checkIfHomeReturns() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.clickOnView(solo.getView(R.id.backIcon));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("HomePage", HomePage.class);
    }

    /**
     * Checks if the edit button works as intended
     */
    @Test
    public void checkEditButton() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);
        solo.clickOnView(solo.getView(R.id.editIcon));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.enterText(0, "Gandalf");
        solo.enterText(1, "Gandalf");
        solo.searchText("Gandalf");
        solo.searchText("Gandalf");
        solo.clickOnButton(1);
        solo.searchText("Gandalf");
        solo.searchText("Gandalf");
        solo.clickOnView(solo.getView(R.id.editIcon));
        solo.clearEditText(0);
        solo.clearEditText(1);
        solo.enterText(0, "Sauron");
        solo.enterText(1, "Sauron");
        solo.clickOnButton(1);
    }

    /**
     * Check if the cancel button works on the edit fragment
     */
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

    /**
     * Checks if all the data present on profile page for
     * user Sauron's Highest QRCode is valid or not
     */
    @Test
    public void checkHighestQRdeets() {
        final Integer[] rank_value = {0};

        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document("Sauron");
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
                                        solo.sleep(2000);
                                        assertEquals(qrCodes.get(0).get("qrname"), ((TextView) solo.getView(R.id.qr_name_text)).getText());
                                        rank_value[0] = (Integer) (qrCodes_test.indexOf(qrCodes.get(0).get("hash"))) + 1;
                                        assertEquals(String.valueOf(rank_value[0]), ((TextView) solo.getView(R.id.qr_ranking)).getText());
                                        double value = (rank_value[0] / (double) qrCodes_test.size()) * 100;
                                        if (value >= 0 && value <= 10) {
                                            assertEquals("Very Rare", ((TextView) solo.getView(R.id.rarity)).getText());

                                        }
                                        if (value > 10 && value <= 30) {
                                            assertEquals("Rare", ((TextView) solo.getView(R.id.rarity)).getText());

                                        }
                                        if (value > 30 && value <= 100) {
                                            assertEquals("Common", ((TextView) solo.getView(R.id.rarity)).getText());
                                        }
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

    /**
     * Checks if all the data present on profile page for
     * user Sauron is valid or not
     */
    @Test
    public void checkUserDeets() {
        solo.clickOnView(solo.getView(R.id.profileButton));
        solo.waitForActivity("UserProfilePage");
        solo.assertCurrentActivity("UserProfilePage", ProfilePage.class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Users").document("Sauron");
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
                            assertEquals(String.valueOf(count), ((TextView) solo.getView(R.id.qrcodes_text)).getText());

                        }
                    } else {
                        Log.d("No Doc", "No such document");
                    }
                } else {
                    Log.d("Err", "get failed with ", task.getException());
                }
            }
        });

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
                    assertEquals(String.valueOf(users_array.get(users_usernames.indexOf("Sauron")).get("Score")), ((TextView) solo.getView(R.id.score_text)).getText());
                    double position = ((users_usernames.indexOf("Sauron") + 1) / (double) users_array.size()) * 100;
                    if (position >= 0 && position <= 10) {
                        assertEquals("Top 10%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 10 && position <= 20) {
                        assertEquals("Top 20%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 20 && position <= 30) {
                        assertEquals("Top 30%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 30 && position <= 40) {
                        assertEquals("Top 40%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 40 && position <= 50) {
                        assertEquals("Top 50%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 50 && position <= 60) {
                        assertEquals("Top 60%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 60 && position <= 70) {
                        assertEquals("Top 70%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 70 && position <= 80) {
                        assertEquals("Top 80%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 80 && position <= 90) {
                        assertEquals("Top 90%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    if (position > 90 && position <= 100) {
                        assertEquals("Top 100%", ((TextView) solo.getView(R.id.rank_text)).getText());
                    }
                    System.out.println(position);

                } else {
                    Log.d("Error: ", "Couldn't get users");
                }
            }
        });
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
