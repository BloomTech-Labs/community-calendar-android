package com.lambda_labs.community_calendar

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.room.RecentSearchDatabase
import io.reactivex.Flowable

class Repository(context: Context) {
    private val recentSearchDatabase by lazy {
        databaseBuilder(context, RecentSearchDatabase::class.java, "recent_search_db").build()
    }

    fun getRecentSearchList(): Flowable<MutableList<Search>>{
        return recentSearchDatabase.dataAccessObject().getRecentSearchList()
    }

    fun addRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().addRecentSearch(search)
    }

    fun updateRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().updateRecentSearch(search)
    }

    fun removeRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().removeRecentSearch(search)
    }
}