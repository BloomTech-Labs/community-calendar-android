package com.lambda_labs.community_calendar.view


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lambda_labs.community_calendar.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {

    @Rule // Rule for JUnit
    @JvmField //For Kotlin compatibility
    var activityScenarioRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java) // To wrap up the activity

    @Test
    fun show_EventDetails_of_any_event() {
        Thread.sleep(2000)
        onView(withId(R.id.btn_grid))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.card_view_grid))
            .perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.btn_attend)).check(matches(withText("Attend")))
        Thread.sleep(2000)
    }
}