package com.lambda_labs.community_calendar.util

import android.annotation.SuppressLint
import com.lambda_labs.community_calendar.SearchValue
import com.lambda_labs.community_calendar.util.Util.getSearchDate
import com.lambda_labs.community_calendar.util.Util.negativeDate

object SearchUtil {
    @SuppressLint("DefaultLocale")
    /*
        Takes in events list and filters the events by the values in searchValue
        filters by title, date, location name, location zipcode, and tags
        title required others are optional
        events must match everything in the filters to be shown
        returns filtered event list
     */
    fun searchEvents(events: ArrayList<EventsQuery.Event>, searchValue: SearchValue): ArrayList<EventsQuery.Event> {
        val filteredEvents = ArrayList<EventsQuery.Event>()
        events.forEach { event ->
            if (searchValue.search.isNotEmpty()) {
                // Gets values to filter with or by
                val lowerSearch = searchValue.search.toLowerCase()
                val title = event.title().toLowerCase()
                val date = event.start().toString()
                val filterDate = getSearchDate(searchValue.date)
                val noDate = getSearchDate(negativeDate())

                // Checks if events contain whats in searchValue
                val titleCheck = title.contains(lowerSearch)

                val dateCheck = (date.contains(filterDate) || filterDate.contains(noDate))

                val location = event.locations()?.any { l1 ->
                    l1.name().toLowerCase().contains(searchValue.location.toLowerCase())
                } ?: true
                val zipcode = event.locations()?.any { l2 ->
                    l2.zipcode() == searchValue.zipcode || searchValue.zipcode == -1
                } ?: true

                // Checks tags and if at least one matches returns true
                fun tags(): Boolean {
                    var match = false
                    event.tags()?.forEach { tag ->
                        searchValue.tags.forEach { searchTag ->
                            val oneTag = tag.title().contains(searchTag)
                            if (oneTag) match = true
                        }
                    }
                    return match
                }

                // Must all return true to be added to the filteredEvents
                if (titleCheck && dateCheck && location && zipcode && tags()) {
                    filteredEvents.add(event)
                }
            }
        }
        return filteredEvents
    }
}