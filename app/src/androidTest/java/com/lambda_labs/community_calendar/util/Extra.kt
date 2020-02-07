package com.lambda_labs.community_calendar.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


object Extra {

    fun getChildAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return (parent is ViewGroup) && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    fun get_resource_id_from_string(idString: String): Int {
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().context
        val packageName: String = targetContext.packageName
        return targetContext.resources.getIdentifier(idString, "id", packageName)
    }
}