package com.lambda_labs.community_calendar.util

import EventsQuery
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonUtil {

    // creates JsonAdapter to convert events to json and vise versa
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(EventsQuery.Event::class.java)

    // turns event into a json string
    fun eventToJson(event: EventsQuery.Event): String {
        return moshi.toJson(event)
    }

    // if the string is not a event json string then it will return null
    fun jsonToEvent(eventJson: String): EventsQuery.Event? {
        var event: EventsQuery.Event? = null
        try {
            event = moshi.fromJson(eventJson)
        } catch (e: com.squareup.moshi.JsonEncodingException){}
        return event
    }

    // key for bundles when passing an event
    fun eventJsonKey() = "jsonEvent"

}