package com.lambda_labs.community_calendar.view

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.lambda_labs.community_calendar.R
import org.hamcrest.Matcher
import org.junit.Before
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

    //val searchViewMatcher: Matcher<View> = instanceOf(CustomSearchView::class.java)
    //val searchViewMatcher: Matcher<View> = withTagValue(`is`("search_bar")) // Must match tag of SearchView
    // Must match hint text of SearchView
    private var searchViewMatcher: Matcher<View> = withHint("Search")

    @Before
    fun navigate_to_search_fragment() {
        Thread.sleep(5000)
        onView(searchViewMatcher)
            .perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun check_back_button_behavior() {
        onView(isRoot())
            .perform(closeSoftKeyboard())
            .perform(pressBack())
            .perform(pressBack())
        Thread.sleep(2000)

        onView(withId(R.id.featured_event_recycler))
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun check_functionality_of_cancel_button() {
        onView(withId(R.id.btn_cancel))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.featured_event_recycler))
            .check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun search_with_manual_entry() {
        val searchString: String = "art"

        onView(searchViewMatcher)
            .perform(typeText(searchString))
            .perform(pressImeActionButton())
        Thread.sleep(1000)

        onView(withId(R.id.txt_searched_by))
            .check(matches(withText("by \"$searchString\"")))
        Thread.sleep(2000)
    }
}






















