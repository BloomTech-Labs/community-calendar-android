package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository
import io.reactivex.disposables.Disposable

class MainActivityViewModel(val repo: Repository) : ViewModel() {

    // Creates LiveData to be observed on the HomeFragment


    // Init a global variable to be able to call it from another function
    private var disposable: Disposable? = null


    fun queryEvents(){
        disposable = repo.getEvents()
    }

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return repo.events
    }

    // Disposes of the disposable to prevent memory leak
    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        super.onCleared()
    }
}