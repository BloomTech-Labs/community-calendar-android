package com.lambda_labs.community_calendar.room

import androidx.room.Database
import com.lambda_labs.community_calendar.model.RecentSearch

@Database(entities = [RecentSearch::class], version = 1, exportSchema = false)
abstract class RecentSearchDatabase {
    abstract fun dataAccessObject(): RecentSearchDao
}