package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.apollo.ApolloClient.client
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val _events = MutableLiveData<List<EventsQuery.Event>>()
    val events: LiveData<List<EventsQuery.Event>> = _events

    private var disposable: Disposable? = null

    fun getEvents() {
        disposable = client().rxQuery(EventsQuery())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith (object: DisposableObserver<Response<EventsQuery.Data>>() {
                override fun onComplete() {

                }

                override fun onNext(t: Response<EventsQuery.Data>) {
                    _events.value  = t.data()?.events()
                }

                override fun onError(e: Throwable) {
                    println(e.message)
                }

            })

    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null){
            disposable?.dispose()
        }
    }
}