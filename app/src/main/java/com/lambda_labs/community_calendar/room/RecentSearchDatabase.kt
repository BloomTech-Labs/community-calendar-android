package com.lambda_labs.community_calendar.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lambda_labs.community_calendar.model.RecentSearch

@Database(entities = [RecentSearch::class], version = 1, exportSchema = false)
abstract class RecentSearchDatabase: RoomDatabase() {
    abstract fun dataAccessObject(): RecentSearchDao
}