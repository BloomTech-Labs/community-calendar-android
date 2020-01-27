package com.lambda_labs.community_calendar.util

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.lambda_labs.community_calendar.R


// Takes in a string and context then it creates a Chip to how we want it to look
private fun createChip(text: String, context: Context): Chip {
    val chip = Chip(context)
    chip.text = text
    val colorTint = ContextCompat.getColor(context, R.color.colorInactive)
    chip.closeIconTint = ColorStateList.valueOf(colorTint)
    chip.closeIconSize = 50f
    chip.chipStartPadding = 18f
    chip.textSize = 16f
    chip.typeface = ResourcesCompat.getFont(context, R.font.poppins_light)
    val textColor = ContextCompat.getColor(context, android.R.color.white)
    chip.setTextColor(textColor)
    chip.closeIcon = ContextCompat.getDrawable(context, R.drawable.chip_close)
    chip.isCloseIconVisible = true
    val colorBackground = ContextCompat.getColor(context, R.color.chipBackground)
    chip.chipBackgroundColor = ColorStateList.valueOf(colorBackground)
    chip.chipMinHeight = 60f

    return chip
}

// Takes in a list of tag names turns them into chips and adds them
fun createChipLayout(tags: List<String>, context: Context, chipParent: ChipGroup){
    tags.forEach {tagName ->
        val chip = createChip(tagName, context)
        chipParent.addView(chip)
        chip.setOnCloseIconClickListener {
            chipParent.removeView(it)
            it as Chip
            tags as MutableList<String>
            tags.remove(it.text)
        }
    }
}