package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.apollo.ApolloClient.client
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val _events = MutableLiveData<List<EventsQuery.Event>>()
    val events: LiveData<List<EventsQuery.Event>> = _events

    fun getEvents() {
        val observable = client().rxQuery(EventsQuery())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onNext ->
                _events.value  = onNext.data()?.events()
            }
    }

}