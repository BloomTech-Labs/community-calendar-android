package com.lambda_labs.community_calendar.util

import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun displayTime(start: Any, end: Any): String {

        val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val formatDate = SimpleDateFormat(format, Locale.getDefault())
        formatDate.timeZone = TimeZone.getTimeZone("GMT")
        val resultStart = formatDate.parse(start.toString()) as Date
        val resultEnd = formatDate.parse(end.toString()) as Date


        fun getTime(date: Date): String {
            val calender = Calendar.getInstance()
            calender.time = date
            val military = calender.get(Calendar.HOUR_OF_DAY)
            val normal = calender.get(Calendar.HOUR)
            return if (military <= 12) {
                "${normal}AM"
            } else {
                "${normal}PM"
            }
        }


        val begin = getTime(resultStart)
        val finish = getTime(resultEnd)
        return "$begin - $finish"
    }
}