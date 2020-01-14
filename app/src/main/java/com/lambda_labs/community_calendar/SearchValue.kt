package com.lambda_labs.community_calendar

import com.lambda_labs.community_calendar.util.Util.negativeDate
import java.util.*

// Temporary model class until Room is implemented
// all values have defaults besides search to make filtering optional
class SearchValue(
    val search: String,
    val location: String = "",
    val zipcode:Int = -1,
    val date: Date = negativeDate(),
    val tags: List<String> = arrayListOf("")
)