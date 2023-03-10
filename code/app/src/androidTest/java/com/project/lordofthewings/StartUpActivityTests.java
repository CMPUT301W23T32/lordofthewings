package com.project.lordofthewings;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;

import com.project.lordofthewings.Views.HomePage;
import com.project.lordofthewings.Views.MainActivity;
import com.project.lordofthewings.Views.StartUpPages.SignUpPage;
import com.project.lordofthewings.Views.StartUpPages.StartUpPage;
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
public class StartUpActivityTests {
    private Solo solo;

    @Rule
    public ActivityTestRule<StartUpPage> rule =
            new ActivityTestRule<>(StartUpPage.class, true, true);
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
    public void checkIfPageSwitchesToSignUp(){

        solo.assertCurrentActivity("Wrong Activity", StartUpPage.class);

        solo.clickOnView(solo.getView(R.id.startup_signup_button));
        solo.waitForActivity("SignUpPage");

        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);

    }


    @Test
    public void checkIfSignUpWorks() {
        solo.assertCurrentActivity("Wrong Activity", StartUpPage.class);
        solo.clickOnView(solo.getView(R.id.startup_signup_button));
        solo.waitForActivity("SignUpPage");
        solo.assertCurrentActivity("Wrong Activity", SignUpPage.class);
        solo.enterText((EditText) solo.getView(R.id.username), "mktest");
        solo.enterText((EditText) solo.getView(R.id.email), "mktest");
        solo.enterText((EditText) solo.getView(R.id.firstName), "mktest");
        solo.enterText((EditText) solo.getView(R.id.lastName), "mktest");
        solo.clickOnView(solo.getView(R.id.signUpButton));
        solo.waitForActivity("HomePage");
        solo.assertCurrentActivity("Wrong Activity", HomePage.class);

    }














    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
