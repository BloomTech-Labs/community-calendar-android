package com.lambda_labs.community_calendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.lambda_labs.community_calendar.util.getSearchDate
import com.lambda_labs.community_calendar.util.negativeDate
import com.lambda_labs.community_calendar.util.searchStringToDate
import java.io.Serializable
import java.util.*

// (primaryKeys = ["searchText", "id"])
@Entity
@TypeConverters(DateConverter::class, ListConverter::class)
data class Search(
    var searchText: String,
    val location: String = "",
    val zipcode: Int = -1,
    val date: Date = negativeDate(),
    val tags: List<String> = arrayListOf(""),
    @PrimaryKey(autoGenerate = true)val id: Int = 0
) : Serializable

class DateConverter {
    @TypeConverter
    fun toDate(stringDate: String): Date {
        return searchStringToDate(stringDate)
    }
    @TypeConverter
    fun toString(date: Date): String {
        return getSearchDate(date)
    }
}

class ListConverter {
    @TypeConverter
    fun toList(stringList: String): List<String>{
        return stringList.split(",")
    }
    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }
}
