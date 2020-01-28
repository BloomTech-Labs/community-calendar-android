package com.lambda_labs.community_calendar

import EventsQuery
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.searchEvents
import com.lambda_labs.community_calendar.util.searchStringToDate
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchUnitTest {
    val location = EventsQuery.Location("Location", 2.0, "Utah", 84601)
    val locations = arrayListOf(location)
    val tag = EventsQuery.Tag("Tag", "BatMan")
    val tags = arrayListOf(tag)
    val event = EventsQuery.Event(
        "Event",
        "1",
        "Free code class",
        "Free class for coders",
        "2020-01-22T03:00:00.000Z",
        "2020-01-22T19:00:00.000Z",
        null,
        null,
        locations,
        tags)
    val events = arrayListOf(event)


    @Test
    fun testCaseSearch() {
        val search = Search("")
        search.searchText = "FREE CODE CLASS"
        val list = searchEvents(events, search)
        assertEquals(1, list.size)
    }

    @Test
    fun eventByDateSearch() {
        val search = Search("free code class", date = searchStringToDate("2020-01-22"))
        val list = searchEvents(events, search)
        assertEquals(1, list.size)
    }

    @Test
    fun eventByZip() {
        val search = Search("free code class", zipcode = 84601)
        val list = searchEvents(events, search)
        assertEquals(1, list.size)
    }

    @Test
    fun eventByLocationName() {
        val search = Search("free code class", location = "Utah")
        val list = searchEvents(events, search)
        assertEquals(1, list.size)
    }

    @Test
    fun eventByTagName() {
        val search = Search("free code class", tags = arrayListOf("batman"))
        val list = searchEvents(events, search)
        assertEquals(1, list.size)
    }




}