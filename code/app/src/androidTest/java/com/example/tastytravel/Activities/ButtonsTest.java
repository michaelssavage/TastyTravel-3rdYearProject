package com.example.tastytravel.Activities;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.tastytravel.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ButtonsTest {

    @Rule
    public ActivityTestRule<StartActivity> mActivityTestRule = new ActivityTestRule<>(StartActivity.class);

    @Test
    public void buttonsTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.continueWithoutAccount), withText("Continue Without An Account"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_about), withContentDescription("About"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavBar),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.menu_home), withContentDescription("Home"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavBar),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.savedPlacesBtn), withContentDescription("image of coffees."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollview),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.historyBtn), withContentDescription("image of beers."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollview),
                                        0),
                                2)));
        appCompatImageView2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.savedPlacesText), withText("My Saved Places"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollview),
                                        0),
                                1)));
        appCompatTextView2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.historyBtn), withContentDescription("image of beers."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollview),
                                        0),
                                2)));
        appCompatImageView3.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.searchBtn), withText("FIND PLACES TO MEET"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.closeText), withText("Close"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.menu_profile), withContentDescription("Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavBar),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
