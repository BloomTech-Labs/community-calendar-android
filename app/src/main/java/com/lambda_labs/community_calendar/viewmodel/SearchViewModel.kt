package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.model.RecentSearch
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel: ViewModel() {
    // Database call will be done in viewmodel
    private var disposable: Disposable? = null
    private val recentSearchList: MutableLiveData<MutableList<RecentSearch>> = MutableLiveData(
        mutableListOf())
    val searchList: LiveData<MutableList<RecentSearch>> = recentSearchList

    // Retrieves searches stored in database and saves them to recentSearchList to pass to searchList for fragment
    fun getRecentSearches(){
        disposable = App.repository.getRecentSearchList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe{ recentSearches: MutableList<RecentSearch> ->
                recentSearchList.value?.clear()
                recentSearchList.value?.addAll(recentSearches)
            }
    }

    fun updateRecentSearch(recentSearch: RecentSearch){
        App.repository.updateRecentSearch(recentSearch)
    }

    fun removeRecentSearch(recentSearch: RecentSearch){
        App.repository.removeRecentSearch(recentSearch)
    }

    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        super.onCleared()
    }
}