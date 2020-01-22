package com.lambda_labs.community_calendar.util

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

// Takes in two strings and makes the second string black and have quotes around it
fun recentSearchDisplayText(searchType: String, value: String): SpannableStringBuilder {
    val spannable = SpannableStringBuilder("$searchType \"$value\"")
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        searchType.length, spannable.length,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return spannable
}