package com.lambda_labs.community_calendar.model

data class Filter(
    var location: Int = -1,
    var zip: String = "",
    var date: String = "",
    var tags: List<String> = listOf()
)