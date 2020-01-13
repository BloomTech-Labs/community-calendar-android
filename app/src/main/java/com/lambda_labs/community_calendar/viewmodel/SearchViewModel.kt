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
    lateinit var disposable: Disposable
    private val recentSearchList: MutableLiveData<MutableList<RecentSearch>> = MutableLiveData(
        mutableListOf())
    val searchList: LiveData<MutableList<RecentSearch>> = recentSearchList

    private fun getRecentSearchList(): Flowable<MutableList<RecentSearch>> {
        return App.repository.getRecentSearchList()
    }

    fun getRecentSearches(){
        disposable = getRecentSearchList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe{ recentSearches: MutableList<RecentSearch> ->
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
        disposable.dispose()
        super.onCleared()
    }
}