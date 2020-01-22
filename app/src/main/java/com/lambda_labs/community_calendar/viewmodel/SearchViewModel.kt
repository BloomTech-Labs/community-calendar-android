package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.negativeDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel: ViewModel() {
    // Database call will be done in viewmodel
    private var disposable: Disposable? = null
    private val recentSearchList: MutableLiveData<MutableList<Search>> = MutableLiveData(
        mutableListOf())
    val searchList: LiveData<MutableList<Search>> = recentSearchList

    // Retrieves searches stored in database and saves them to recentSearchList to pass to searchList for fragment
    fun getRecentSearches(){
        disposable = App.repository.getRecentSearchList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe{ searches: MutableList<Search> ->
                recentSearchList.value?.clear()
                recentSearchList.value?.addAll(searches)
            }
    }

    fun updateRecentSearch(search: Search){
        App.repository.updateRecentSearch(search)
    }

    fun removeRecentSearch(search: Search){
        App.repository.removeRecentSearch(search)
    }

    /* Checks filters to see if the user changed any then adds them
    to a list which is used in RecentSearchRecycler */
    fun searchToSearchList(search: Search): ArrayList<Any> {
        val list = ArrayList<Any>()
        val location = search.location
        if (location.isNotEmpty()) list.add(location)
        val zipcode = search.zipcode
        if (zipcode != -1) list.add(zipcode)
        val date = search.date
        if (date != negativeDate()) list.add(date)
        val tags = search.tags
        if (tags[0].isNotEmpty()) list.add(tags)

        return list
    }
    // Checks search to see if any filters have been added
    fun hasNoFilters(search: Search): Boolean {
        val checkLocation = search.location.isEmpty()
        val checkZipcode = search.zipcode == -1
        val checkDate = search.date == negativeDate()
        val checkTags = search.tags[0].isEmpty()
        return checkLocation && checkZipcode && checkDate && checkTags
    }

    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        super.onCleared()
    }
}