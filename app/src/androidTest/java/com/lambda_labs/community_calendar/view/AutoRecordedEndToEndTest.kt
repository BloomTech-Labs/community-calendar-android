package com.lambda_labs.community_calendar.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.lambda_labs.community_calendar.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AutoRecordedEndToEndTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        Thread.sleep(2000)
        val cardView = onView(
            allOf(
                withId(R.id.featured_event_card_view),
                childAtPosition(
                    allOf(
                        withId(R.id.featured_event_recycler),
                        childAtPosition(
                            withId(R.id.home_layout),
                            2
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val materialButton = onView(
            allOf(
                withId(R.id.btn_attend), withText("Attend"),
                childAtPosition(
                    allOf(
                        withId(R.id.scrollview_constraint),
                        childAtPosition(
                            withId(R.id.scrollview),
                            0
                        )
                    ),
                    7
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_map), withText("Open in maps"),
                childAtPosition(
                    allOf(
                        withId(R.id.scrollview_constraint),
                        childAtPosition(
                            withId(R.id.scrollview),
                            0
                        )
                    ),
                    8
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.loginFragment), withContentDescription("Log In"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.home), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val cardView2 = onView(
            allOf(
                withId(R.id.featured_event_card_view),
                childAtPosition(
                    allOf(
                        withId(R.id.featured_event_recycler),
                        childAtPosition(
                            withId(R.id.home_layout),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView2.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_attend), withText("Attend"),
                childAtPosition(
                    allOf(
                        withId(R.id.scrollview_constraint),
                        childAtPosition(
                            withId(R.id.scrollview),
                            0
                        )
                    ),
                    7
                )
            )
        )
        materialButton3.perform(scrollTo(), click())

        val cardView3 = onView(
            allOf(
                withId(R.id.featured_event_card_view),
                childAtPosition(
                    allOf(
                        withId(R.id.featured_event_recycler),
                        childAtPosition(
                            withId(R.id.home_layout),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView3.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_attend), withText("Attend"),
                childAtPosition(
                    allOf(
                        withId(R.id.scrollview_constraint),
                        childAtPosition(
                            withId(R.id.scrollview),
                            0
                        )
                    ),
                    7
                )
            )
        )
        materialButton4.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
