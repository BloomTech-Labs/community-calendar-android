package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedSearchViewModel: ViewModel() {
    private val _list = MutableLiveData<List<EventsQuery.Event>>()

    fun getFilteredList(): LiveData<List<EventsQuery.Event>> {
        return _list
    }

    fun addList(list: List<EventsQuery.Event>) {
        _list.value = list
    }
}