package com.lambda_labs.community_calendar

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room.databaseBuilder
import com.lambda_labs.community_calendar.model.RecentSearch
import com.lambda_labs.community_calendar.room.RecentSearchDatabase
import io.reactivex.Flowable

class Repository(context: Context) {
    private val recentSearchDatabase by lazy {
        databaseBuilder(context, RecentSearchDatabase::class.java, "recent_search_db").build()
    }

    fun getRecentSearchList(): Flowable<MutableList<RecentSearch>>{
        return recentSearchDatabase.dataAccessObject().getRecentSearchList()
    }

    fun addRecentSearch(recentSearch: RecentSearch){
        recentSearchDatabase.dataAccessObject().addRecentSearch(recentSearch)
    }

    fun updateRecentSearch(recentSearch: RecentSearch){
        recentSearchDatabase.dataAccessObject().updateRecentSearch(recentSearch)
    }

    fun removeRecentSearch(recentSearch: RecentSearch){
        recentSearchDatabase.dataAccessObject().removeRecentSearch(recentSearch)
    }
}