package com.project.lordofthewings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.QRCodePage;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCodePageTests {

    private Solo solo;
    SharedPreferences sh1 = InstrumentationRegistry.getInstrumentation().getTargetContext().getSharedPreferences("sharedPrefs", 0);
    String savedusername =  sh1.getString("username", "");


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


}
