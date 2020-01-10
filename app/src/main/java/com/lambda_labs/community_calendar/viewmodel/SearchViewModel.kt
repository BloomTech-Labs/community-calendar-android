package com.lambda_labs.community_calendar.viewmodel

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.model.RecentSearch
import io.reactivex.Flowable

class SearchViewModel: ViewModel() {
    fun getRecentSearchList(): Flowable<MutableList<RecentSearch>> {
        return App.repository.getRecentSearchList()
    }

    fun addRecentSearch(recentSearch: RecentSearch){
        App.repository.addRecentSearch(recentSearch)
    }

    fun updateRecentSearch(recentSearch: RecentSearch){
        App.repository.updateRecentSearch(recentSearch)
    }

    fun removeRecentSearch(recentSearch: RecentSearch){
        App.repository.removeRecentSearch(recentSearch)
    }
}

class GetRecentSearchAysnc(context: Context): AsyncTask<Void, Void, LiveData<MutableList<RecentSearch>>>(){
    override fun doInBackground(vararg p0: Void?): LiveData<MutableList<RecentSearch>> {
        TODO() // return App.repository.getRecentSearchList()
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
        TODO() // return App.repository.addRecentSearch(recentSearch)
    }
}

class UpdateRecentSearchAysnc(private val recentSearch: RecentSearch): AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?): Unit {
        TODO() // return App.repository.updateRecentSearch(recentSearch)
    }
}

class DeleteRecentSearchAysnc(private val recentSearch: RecentSearch): AsyncTask<Void, Void, Unit>(){
    override fun doInBackground(vararg p0: Void?): Unit {
        TODO() // return App.repository.removeRecentSearch(recentSearch)
    }
}