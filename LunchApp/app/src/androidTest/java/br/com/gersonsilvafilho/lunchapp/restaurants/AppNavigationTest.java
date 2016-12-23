/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.gersonsilvafilho.lunchapp.restaurants;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.gersonsilvafilho.lunchapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Tests for the {@link DrawerLayout} layout component in {@link RestaurantActivity} which manages
 * navigation within the app.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch your activity under test.
     *
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<RestaurantActivity> mActivityTestRule =
            new ActivityTestRule<>(RestaurantActivity.class);

    @Test
    public void clickOnTomorrowNav_ShowsTomorrowTab() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start tomorrow view
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.nav_tomorrow));

       // Check that statistics Activity was opened.
        String expectedTomorrowToolbarText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.nav_tomorrow_text);


        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedTomorrowToolbarText)));
    }

    @Test
    public void clickOnYesterdayNav_ShowsYesterdayTab() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start tomorrow view
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.nav_yesterday));

        // Check that statistics Activity was opened.
        String expectedYesterdayToolbarText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.nav_yesterday_text);


        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedYesterdayToolbarText)));
    }

    @Test
    public void clickOnHomeNav_ShowsHomeTab() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        // Start tomorrow view
        onView(withId(R.id.nav_view))
                .perform(navigateTo(R.id.nav_home));

        // Check that statistics Activity was opened.
        String expectedTodayToolbarText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.nav_today_text);


        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedTodayToolbarText)));
    }

    @Test
    public void clickOnAndroidHomeIcon_OpensNavigation() {
        // Check that left drawer is closed at startup
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))); // Left Drawer should be closed.

        // Open Drawer
        onView(withId(R.id.drawer_layout)).perform(open());

        // Check if drawer is open
        onView(withId(R.id.drawer_layout))
                .check(matches(isOpen(Gravity.LEFT))); // Left drawer is open open.
    }

}