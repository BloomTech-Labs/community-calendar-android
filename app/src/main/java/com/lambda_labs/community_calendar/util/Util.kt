package com.lambda_labs.community_calendar.util

import java.text.SimpleDateFormat
import java.util.*

object Util {

    /*
        displayTime function:
            Takes in start and end time date that looks like "2019-12-22T17:00:00.000Z"
            and returns a string formatted like "10:00AM - 12:30PM"
     */

    fun displayTime(start: Any, end: Any): String {
        // Formats the strings to dates
        val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val formatDate = SimpleDateFormat(format, Locale.getDefault())
        formatDate.timeZone = TimeZone.getTimeZone("GMT")
        val resultStart = formatDate.parse(start.toString()) as Date
        val resultEnd = formatDate.parse(end.toString()) as Date

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
}