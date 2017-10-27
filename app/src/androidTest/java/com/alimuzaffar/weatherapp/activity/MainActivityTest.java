package com.alimuzaffar.weatherapp.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.TextView;

import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.db.RecentLocationsHelper;
import com.alimuzaffar.weatherapp.model.RecentLocation;
import com.alimuzaffar.weatherapp.util.AppSettings;

import java.util.List;

/**
 * Tests for MainActivity.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mTestActivity;
    private TextView mTestEmptyText;
    private FloatingActionButton mFab;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mTestActivity = getActivity();
        mTestEmptyText = (TextView) mTestActivity.findViewById(R.id.empty);
        mFab = (FloatingActionButton) mTestActivity.findViewById(R.id.fab);
    }

    /**
     * Test if your test fixture has been set up correctly. You should always implement a test that
     * checks the correct setup of your test fixture. If this tests fails all other tests are
     * likely to fail as well.
     */
    public void testPreconditions() {
        //Try to add a message to add context to your assertions. These messages will be shown if
        //a tests fails and make it easy to understand why a test failed
        assertNotNull("mTestActivity is null", mTestActivity);
        assertNotNull("mTestEmptyText is null", mTestEmptyText);
        assertNotNull("mFab is null", mFab);
    }

    /**
     * Tests the correctness of the initial text.
     */
    public void testEmptyView_labelText() {
        //It is good practice to read the string from your resources in order to not break
        //multiple tests when a string changes.
        AppSettings settings = AppSettings.getInstance(mTestActivity);
        String expected = mTestActivity.getString(R.string.help_text);
        if (settings.getLong(AppSettings.Key.CURRENT_LOCATION_ID) != 0) {
            expected = mTestActivity.getString(R.string.loading_please_wait);
        }
        final String actual = mTestEmptyText.getText().toString();
        assertEquals("mTestEmptyText contains wrong text", expected, actual);
    }

    /**
     * Use espresso to check Fab click to see if the next activity displays.
     */
    public void testFab_onClick() {
        Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.txt_search)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    public void testDatabaseMethods_add() {
        RecentLocationsHelper.addLocationToDB(123456, "Hello World");
        RecentLocation r = RecentLocationsHelper.getLocation(123456);
        assertEquals("Ids do not match", 123456, r.getId());
        assertEquals("Names do not match", "Hello World", r.getName());
    }

    public void testDatabaseMethods_all() {
        List<RecentLocation> locs = RecentLocationsHelper.getAllLocations();
        assertTrue("Locations are empty", locs.size() > 0);
        boolean found = false;
        for (RecentLocation l : locs) {
            if (l.getId() == 123456 && l.getName().equals("Hello World")) {
                found = true;
            }
        }
        assertTrue("Entry not found in read all", found);
    }

    public void testDatabaseMethods_delete() {
        boolean result = RecentLocationsHelper.deleteLocation(123456);
        assertTrue("Delete failed", result);

    }

}
