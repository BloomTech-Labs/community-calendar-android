package com.lambda_labs.community_calendar.util

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.lambda_labs.community_calendar.R

object ViewUtil {

    /**
     * Build a component from scratch with necessary values to create an Input Chip.
     * @param   context     The context where the Chip will be located
     * @param   added       Boolean value whether the Chip to create is a tag that has been added
     * @param   pxHeight    Integer value of the pixel size that the height of the Chip should be
     * @return              The Chip component that has been created
     * @see Material        https://material.io/develop/android/components/chip/
     * @see Material        https://material.io/components/chips/
     * @see Medium          https://medium.com/over-engineering/hands-on-with-material-components-for-android-chips-21dc67c8b956
     */
    fun generateChip(context: Context, added: Boolean, pxHeight: Int): Chip {
        val chip: Chip = Chip(context)
        chip.isClickable = false
        chip.isCheckable = false
        chip.isChipIconVisible = true
        chip.isCloseIconVisible = true
        chip.isCheckedIconVisible = false
        chip.isCheckedIconVisible = false
        chip.textSize = 13f
        chip.typeface = ResourcesCompat.getFont(context, R.font.poppins_regular)
        chip.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            pxHeight
        )
        chip.setCloseIconTintResource(R.color.colorInactive)

        val icon: Int =
            if (added) R.drawable.ic_cancel_circle_24dp else R.drawable.ic_add_circle_24dp
        chip.setCloseIconResource(icon)

        val chipColor: Int = if (added) R.color.colorAdd else R.color.colorSuggest
        chip.setChipBackgroundColorResource(chipColor)

        val textColor: String = if (added) "#FFFFFF" else "#ACAAAA"
        chip.setTextColor(Color.parseColor(textColor))

        return chip
    }

    /**
     * Takes in an integer in DP (Display Pixels) and converts it to regular pixels.
     * This conversion is necessary because the pixel resolution varies among devices.
     * @param   dp  Integer value representing Display Pixels
     * @param   dM  DisplayMetrics from the ApplicationContext's resources
     * @return      Integer value representing the regular pixels
     */
    fun dpToPx(dp: Int, dM: DisplayMetrics): Int {
        val dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), dM)

        return dimension.toInt()
    }
}