package com.lambda_labs.community_calendar.room

import androidx.room.*
import com.lambda_labs.community_calendar.model.Search
import io.reactivex.Flowable

@Dao
interface RecentSearchDao {
    @Query("SELECT * FROM Search")
    fun getRecentSearchList(): Flowable<MutableList<Search>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecentSearch(search: Search)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecentSearch(search: Search)

    @Delete
    fun removeRecentSearch(search: Search)
}