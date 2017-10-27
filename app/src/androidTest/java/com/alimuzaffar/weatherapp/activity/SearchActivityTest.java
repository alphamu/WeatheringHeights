package com.alimuzaffar.weatherapp.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.widget.DelayAutoCompleteTextView;

/**
 * Tests for MainActivity.
 */
public class SearchActivityTest extends ActivityInstrumentationTestCase2<SearchActivity> {

    private SearchActivity mTestActivity;
    private DelayAutoCompleteTextView mTestSearchView;
    private View mTestClear;

    public SearchActivityTest() {
        super(SearchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mTestActivity = getActivity();
        mTestSearchView = (DelayAutoCompleteTextView) mTestActivity.findViewById(R.id.txt_search);
        mTestClear = mTestActivity.findViewById(R.id.btn_clear);
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
        assertNotNull("mTestSearchView is null", mTestSearchView);
        assertNotNull("mTestClear is null", mTestClear);
    }

    /**
     * Tests the correctness of the initial text.
     */
    public void testSearchView_hintText() {
        //It is good practice to read the string from your resources in order to not break
        //multiple tests when a string changes.
        String expected = mTestActivity.getString(R.string.search_hint);
        final String actual = mTestSearchView.getHint().toString();
        assertEquals("mTestSearchView contains wrong text", expected, actual);
    }

    /**
     * Use espresso to set text for search.
     */
    public void testSearchView_textChange() {
        Espresso.onView(ViewMatchers.withId(R.id.txt_search)).perform(ViewActions.typeText("Hi"));
        assertEquals("Text not set", "Hi", mTestSearchView.getText().toString());
    }

    /**
     * Use espresso to clear search.
     */
    public void testSearchView_textClear() {
        Espresso.onView(ViewMatchers.withId(R.id.btn_clear)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.txt_search)).perform(ViewActions.typeText("Hi"));
        Espresso.onView(ViewMatchers.withId(R.id.btn_clear)).perform(ViewActions.click());
        assertEquals("Text not set", "", mTestSearchView.getText().toString());
    }
    
}
