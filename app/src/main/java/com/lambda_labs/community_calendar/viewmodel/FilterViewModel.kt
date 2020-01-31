package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository

class FilterViewModel(private val repo: Repository) : ViewModel() {

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return repo.events
    }
}