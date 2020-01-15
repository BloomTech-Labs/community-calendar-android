package com.lambda_labs.community_calendar.room

import androidx.room.*
import com.lambda_labs.community_calendar.model.RecentSearch
import io.reactivex.Flowable

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM RecentSearch")
    fun getRecentSearchList(): Flowable<MutableList<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentSearch(recentSearch: RecentSearch)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecentSearch(recentSearch: RecentSearch)

    @Delete
    fun removeRecentSearch(recentSearch: RecentSearch)
}