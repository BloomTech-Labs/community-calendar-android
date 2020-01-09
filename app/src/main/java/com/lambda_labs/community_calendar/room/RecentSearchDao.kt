package com.lambda_labs.community_calendar.room

import androidx.room.*
import com.lambda_labs.community_calendar.model.RecentSearch

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM RecentSearch")
    fun getSearchHistoryList(): MutableList<RecentSearch>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSearches(recentSearch: RecentSearch)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecentSearch(recentSearch: RecentSearch)

    @Delete
    fun removeRecentSearch(recentSearch: RecentSearch)
}