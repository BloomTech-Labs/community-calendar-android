package com.lambda_labs.community_calendar.view

import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.ClickChipCloseIcon
import com.lambda_labs.community_calendar.util.Extra.getChildAtPosition
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
@LargeTest
class FilterFragmentTest {

    @Rule // Rule for JUnit
    @JvmField // For Kotlin compatibility
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java) // To wrap up the activity

    companion object {
        private const val APPLIED_FILTER_COUNT_TEXT = "Filters (1)"
    }

    @Before
    fun navigate_to_the_filter_fragment() {
        Thread.sleep(5000)
        onView(withId(R.id.search_bar))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.btn_filters))
            .perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun select_one_chip_and_apply() {
        val chipGroupSuggestedMatcher: Matcher<View> =
            withId(R.id.chip_group_fragment_filter_suggested)
        val chipStartCount = 10
        val chipIndexToShift: Int = Random.nextInt(0, chipStartCount)
        onView(chipGroupSuggestedMatcher)
            .check(matches(isDisplayed()))
            .check(matches(hasChildCount(chipStartCount)))
        Thread.sleep(1000)

        val chipMatcher: Matcher<View> =
            getChildAtPosition(chipGroupSuggestedMatcher, chipIndexToShift)
        val clickChipCloseIcon: ViewAction = ClickChipCloseIcon()
        onView(chipMatcher)
            .perform(clickChipCloseIcon)
        onView(chipGroupSuggestedMatcher)
            .check(matches(hasChildCount(chipStartCount - 1)))
        onView(withId(R.id.chip_group_fragment_filter_added))
            .check(matches(isDisplayed()))
            .check(matches(hasChildCount(1)))
        Thread.sleep(1000)

        onView(withId(R.id.button_fragment_filter_apply))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.filter_count))
            .check(matches(withText(APPLIED_FILTER_COUNT_TEXT)))
        Thread.sleep(1000)
    }

    @Test
    fun select_a_location_and_apply() {
        onView(withId(R.id.spinner_fragment_filter_location))
            .perform(click())

        val className = "android.widget.PopupWindow\$PopupBackgroundView"
        onData(Matchers.anything())
            .inAdapterView(getChildAtPosition(withClassName(Matchers.`is`(className)), 0))
            .atPosition(1)
            .perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.button_fragment_filter_apply))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.filter_count))
            .check(matches(withText(APPLIED_FILTER_COUNT_TEXT)))
        Thread.sleep(1000)
    }

    @Test
    fun type_in_a_zip_code_and_apply() {
        onView(withId(R.id.edit_text_fragment_filter_zip_code))
            .perform(typeText("98207"))
        Thread.sleep(1000)

        onView(isRoot())
            .perform(closeSoftKeyboard())
        Thread.sleep(1000)

        onView(withId(R.id.button_fragment_filter_apply))
            .perform(click())
        Thread.sleep(2000)

        onView(withId(R.id.filter_count))
            .check(matches(withText(APPLIED_FILTER_COUNT_TEXT)))
        Thread.sleep(1000)
    }
}