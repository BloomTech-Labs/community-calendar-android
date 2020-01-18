package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.Repository
import com.lambda_labs.community_calendar.model.Search
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(val repo: Repository): ViewModel() {
    // Database call will be done in viewmodel
    private var disposable: Disposable? = null
    private val recentSearchList: MutableLiveData<MutableList<Search>> = MutableLiveData(
        mutableListOf())
    val searchList: LiveData<MutableList<Search>> = recentSearchList

    // Retrieves searches stored in database and saves them to recentSearchList to pass to searchList for fragment
    fun getRecentSearches(){
        disposable = repo.getRecentSearchList().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe{ searches: MutableList<Search> ->
                recentSearchList.value?.clear()
                recentSearchList.value?.addAll(searches)
            }
    }

    fun updateRecentSearch(search: Search){
        repo.updateRecentSearch(search)
    }

    fun removeRecentSearch(search: Search){
        repo.removeRecentSearch(search)
    }

    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        super.onCleared()
    }
}