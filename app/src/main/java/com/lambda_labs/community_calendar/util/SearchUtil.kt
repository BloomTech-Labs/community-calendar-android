package com.lambda_labs.community_calendar.util

import EventsQuery
import android.annotation.SuppressLint
import com.lambda_labs.community_calendar.model.Search

@SuppressLint("DefaultLocale")
    /*
        Takes in events list and filters the events by the values in searchValue
        filters by title, date, location name, location zipcode, and tags
        title required others are optional
        events must match everything in the filters to be shown
        returns filtered event list
     */
    fun searchEvents(events: ArrayList<EventsQuery.Event>, search: Search): ArrayList<EventsQuery.Event> {
        val filteredEvents = ArrayList<EventsQuery.Event>()
        events.forEach { event ->
            // Gets values to filter with or by
            val lowerSearch = search.searchText.toLowerCase()
            val title = event.title().toLowerCase()
            val date = event.start().toString()
            val filterDate = getSearchDate(search.date)
            val noDate = getSearchDate(negativeDate())

            // Checks if events contain whats in searchValue
            val titleCheck = (title.contains(lowerSearch) || lowerSearch.contains("filters ("))

            val dateCheck = (date.contains(filterDate) || filterDate.contains(noDate))

            val location = event.locations()?.any { l1 ->
                l1.name().toLowerCase().contains(search.location.toLowerCase())
            } ?: true
            val zipcode = event.locations()?.any { l2 ->
                l2.zipcode() == search.zipcode || search.zipcode == -1
            } ?: true

                // Checks tags and if at least one matches returns true
                fun tags(): Boolean {
                    var match = false
                    if (event.tags() == null) return true
                    event.tags()?.forEach { tag ->
                        search.tags.forEach { searchTag ->
                            val tagTitle = tag.title().toLowerCase()
                            val oneTag = tagTitle.contains(searchTag.toLowerCase())
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
        return filteredEvents
    }
