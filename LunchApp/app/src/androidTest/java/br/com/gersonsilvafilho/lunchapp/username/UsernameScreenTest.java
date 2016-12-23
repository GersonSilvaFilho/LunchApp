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

package br.com.gersonsilvafilho.lunchapp.username;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.gersonsilvafilho.lunchapp.R;
import br.com.gersonsilvafilho.lunchapp.util.UserInfo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;


@LargeTest
public class UsernameScreenTest {

    @Rule
    public ActivityTestRule<UsernameActivity> mActivityTestRule =
            new ActivityTestRule<>(UsernameActivity.class);

    @Before
    public void setupUsername() {
        UserInfo.INSTANCE.setUsername("");
    }

    @Test
    public void enterUsername_ShowsHomeTab() {
        onView(withId(R.id.username_textview)).perform(typeText("Username"), closeSoftKeyboard());

        onView(withId(R.id.email_sign_in_button)).perform(click());

        String expectedTodayToolbarText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.nav_today_text);

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(expectedTodayToolbarText)));
    }
}