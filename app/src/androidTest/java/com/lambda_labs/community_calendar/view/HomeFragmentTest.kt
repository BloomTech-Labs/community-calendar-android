package com.lambda_labs.community_calendar.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.adapter.EventRecycler
import com.lambda_labs.community_calendar.adapter.FeaturedRecycler
import com.lambda_labs.community_calendar.util.ImageViewMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeFragmentTest {

    @Rule // Rule for JUnit
    @JvmField //For Kotlin compatibility
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java) // To wrap up the activity

    @Test
    fun check_Main_Events() {
        Thread.sleep(2000)
        onView(withId(R.id.txt_events_all_upcoming))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.main_event_recycler))
            .perform(swipeUp())
            .perform(swipeUp())
            .perform(swipeUp())
            .perform(swipeDown())
            .perform(swipeDown())
            .perform(swipeDown())
        Thread.sleep(2000)

        onView(withId(R.id.btn_grid))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.main_event_recycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<EventRecycler.ViewHolder>(
                    0,
                    click()
                )
            )
        Thread.sleep(2000)

        onView(withId(R.id.btn_attend)).check(matches(withText(R.string.attend)))
        Thread.sleep(2000)
    }


    @Test
    fun check_Featured_Events() {
        Thread.sleep(5000)
        onView(withId(R.id.featured_event_recycler))
            .perform(swipeLeft())
            .perform(swipeLeft())
            .perform(swipeRight())
            .perform(swipeRight())
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<FeaturedRecycler.ViewHolder>(
                    0,
                    click()
                )
            )
        Thread.sleep(2000)

        onView(withId(R.id.event_image)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.btn_follow))
            .check(matches(withText(R.string.follow_nhost)))
        Thread.sleep(2000)
    }

    @Test
    fun check_See_All_Events() {
        Thread.sleep(2000)
        onView(withId(R.id.txt_see_all))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.txt_searched_by))
            .check(matches(withText("by \"All Featured Events\"")))
        onView(withId(R.id.result_back_button))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.btn_list))
            .check(matches(isDisplayed()))
            .check(matches(ImageViewMatcher.withDrawable(R.drawable.list_view_selected)))
        Thread.sleep(2000)
    }
}