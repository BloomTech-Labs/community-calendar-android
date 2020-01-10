package com.lambda_labs.community_calendar

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room.databaseBuilder
import com.lambda_labs.community_calendar.model.RecentSearch
import com.lambda_labs.community_calendar.room.RecentSearchDatabase

class Repository(context: Context) {
    private val recentSearchDatabase by lazy {
        databaseBuilder(context, RecentSearchDatabase::class.java, "recent_search_db").build()
    }

    fun getRecentSearchList(): LiveData<MutableList<RecentSearch>>{
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

class GetRecentSearchAysnc(context: Context): AsyncTask<Void, Void, LiveData<MutableList<RecentSearch>>>(){
    override fun doInBackground(vararg p0: Void?): LiveData<MutableList<RecentSearch>> {
        TODO() // return Repository().getRecentSearchList()
    }

    interface RecentSearchRecycler{
        fun getRecentSearches(recentSearches: LiveData<MutableList<RecentSearch>>)
    }

    private var listener: RecentSearchRecycler? = null

    init {
        if (context is RecentSearchRecycler){
            listener = context
        }
    }
    override fun onPostExecute(result: LiveData<MutableList<RecentSearch>>?) {
        super.onPostExecute(result)
        if(result != null){
            // setup listener function to go off in activity or fragment
            listener?.getRecentSearches(result)
        }
    }
}

class AddRecentSearchAysnc(private val recentSearch: RecentSearch): AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?): Unit {
        TODO() // return Repository().addRecentSearch(recentSearch)
    }
}

class UpdateRecentSearchAysnc(private val recentSearch: RecentSearch): AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?): Unit {
        TODO() // return Repository().updateRecentSearch(recentSearch)
    }
}

class DeleteRecentSearchAysnc(private val recentSearch: RecentSearch): AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?): Unit {
        TODO() // return Repository().removeRecentSearch(recentSearch)
    }
}