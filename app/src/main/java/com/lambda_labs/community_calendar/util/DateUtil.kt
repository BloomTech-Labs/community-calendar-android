package com.lambda_labs.community_calendar.util

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/*
    stringToDate function:
        Takes in a string that is formatted like the format variable
        then converts it to a date and returns the date
 */

fun stringToDate(string: String): Date {
    val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val formatDate = SimpleDateFormat(format, Locale.getDefault())
    formatDate.timeZone = TimeZone.getTimeZone("GMT")
    return formatDate.parse(string) as Date
}

// takes in string like 2020-01-09 and converts it to a date
fun searchStringToDate(string: String): Date {
    val format = "yyyy-MM-dd"
    val formatDate = SimpleDateFormat(format, Locale.getDefault())
    return formatDate.parse(string) as Date
}

/*
    displayTime function:
        Takes in start and end time date that looks like "2019-12-22T17:00:00.000Z"
        and returns a string formatted like "10:00AM - 12:30PM"
 */

fun displayTime(start: Any, end: Any): String {
    // Formats the strings to dates
    val resultStart = stringToDate(start.toString())
    val resultEnd = stringToDate(end.toString())

    // Gets the time from the date in 12 hour format
    fun getTime(date: Date): String {
        val calender = Calendar.getInstance()
        calender.time = date
        val military = calender.get(Calendar.HOUR_OF_DAY)
        val normal = calender.get(Calendar.HOUR)
        var minutes = calender.get(Calendar.MINUTE).toString()
        if (minutes == "0") minutes = "00"
        return when {
            military < 12 -> {
                "${normal}:${minutes}AM"
            }
            military == 12 -> {
                "12:${minutes}PM"
            }
            else -> {
                "${normal}:${minutes}PM"
            }
        }
    }

    val begin = getTime(resultStart)
    val finish = getTime(resultEnd)
    return "$begin - $finish"
}

// getToday returns today as type Date
fun getToday(): Date {
    return Calendar.getInstance().time
}

// getTomorrow returns tomorrow as type Date
fun getTomorrow(): Date {
    val calender = Calendar.getInstance()
    calender.add(Calendar.DAY_OF_YEAR, 1)
    return calender.time
}

// getWeekendDates returns current weeks weekend as a list of three dates
fun getWeekendDates(): List<Date> {
    val thisWeekend = ArrayList<Date>()
    val calendar = Calendar.getInstance()
    var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    var daysAway = -1

    while (dayOfWeek != Calendar.SATURDAY) {
        dayOfWeek++
        daysAway++
    }

    // why is sunday the first day of the week
    if (daysAway == 5) daysAway = -2

    for (j in 0..2) {
        calendar.add(Calendar.DAY_OF_YEAR, daysAway)
        daysAway = 1
        thisWeekend.add(calendar.time)
    }
    return thisWeekend
}

// Takes in a date and returns it as a string looking like Thursday, January 9, 2020
fun getDisplayDay(date: Date): String {
    val theDay = Calendar.getInstance()
    theDay.time = date
    fun intName(format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(theDay.time)
    }

    val month = intName("MMMM")
    val dayOfWeek = intName("EEEE")
    val day = theDay.get(Calendar.DATE)
    val year = theDay.get(Calendar.YEAR)

    return "$dayOfWeek, $month $day, $year"
}

// Takes in a date and returns it as a string looking like 2020-01-09
fun getSearchDate(date: Date): String {
    val cal = Calendar.getInstance()
    cal.time = date
    val year = cal.get(Calendar.YEAR)
    var day = cal.get(Calendar.DATE).toString()
    if (day.length == 1) day = "0$day"
    var month = (cal.get(Calendar.MONTH) + 1).toString()
    if (month.length == 1) month = "0$month"
    return "$year-$month-$day"

}

fun negativeDate(): Date {
    val date = "1998-03-12"
    return searchStringToDate(date)
}

// Takes in a date and returns it as a string looking like 01-09-2020
fun getRecentSearchDisplay(date: Date): String{
    val splitDate = getSearchDate(date).split("-").toMutableList()
    splitDate.add(splitDate[0])
    splitDate.removeAt(0)
    return splitDate.joinToString("-")



}

// Takes in integer values for the date and then uses another function to get the string
fun parseDate(year: Int, month: Int, day: Int): String {
    val cal = Calendar.getInstance()
    cal.set(year, month, day)

    return getSearchDate(cal.time)
}

// Convert a Date object into a Calendar object
fun toCalendar(date: Date): Calendar {
    val calendar:Calendar = Calendar.getInstance()
    calendar.time = date

    return calendar
}