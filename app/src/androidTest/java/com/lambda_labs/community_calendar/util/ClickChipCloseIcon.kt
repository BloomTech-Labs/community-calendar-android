package com.lambda_labs.community_calendar.util

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.google.android.material.chip.Chip
import org.hamcrest.Matcher


/**
 * Click the close icon for the Chip that this action is being performed on
 */
class ClickChipCloseIcon : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return isAssignableFrom(Chip::class.java)
    }

    override fun getDescription(): String {
        return "Clicked close icon on chip"
    }

    override fun perform(uiController: UiController, view: View) {
        val chip: Chip = view as Chip//we matched
        chip.performCloseIconClick()
    }
}