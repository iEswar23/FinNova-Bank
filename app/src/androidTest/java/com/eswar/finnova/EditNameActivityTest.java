package com.eswar.finnova;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.eswar.finnova.ui.profile.name.EditNameActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * UI test on an emulator/device: Android Studio → right-click → Run 'EditNameActivityTest'.
 */
@RunWith(AndroidJUnit4.class)
public class EditNameActivityTest {

    @Rule
    public ActivityScenarioRule<EditNameActivity> rule = new ActivityScenarioRule<>(EditNameActivity.class);

    @Test
    public void invalidName_showsErrorMessage() throws InterruptedException {
        Thread.sleep(1000); // wait for the fake network call. Practice: replace with an IdlingResource.
        onView(withId(R.id.nameInput)).perform(replaceText("Alex123"), closeSoftKeyboard());
        onView(withId(R.id.saveButton)).perform(click());
        onView(withId(R.id.nameLayout)).check(matches(hasDescendant(withText(R.string.err_name_invalid))));
    }
}
