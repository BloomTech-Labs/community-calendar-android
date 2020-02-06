package com.lambda_labs.community_calendar.view

import android.R.id
import android.content.Context
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.lambda_labs.community_calendar.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {

    @Rule // Rule for JUnit
    @JvmField //For Kotlin compatibility
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java) // To wrap up the activity

    @Test
    fun back_button_behavior() {
        Thread.sleep(5000)
        onView(withId(R.id.featured_event_recycler)).perform(click())
        Thread.sleep(2000)

        onView(ViewMatchers.isRoot()).perform(pressBack())
        Thread.sleep(200)

        onView(withId(R.id.featured_event_recycler)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }
}