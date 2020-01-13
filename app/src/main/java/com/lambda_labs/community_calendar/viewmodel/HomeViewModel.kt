package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.apollo.ApolloClient.client
import com.lambda_labs.community_calendar.model.RecentSearch
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    // Creates LiveData to be observed on the HomeFragment
    private val _events = MutableLiveData<List<EventsQuery.Event>>()
    val events: LiveData<List<EventsQuery.Event>> = _events

    // Init a global variable to be able to call it from another function
    private var disposable: Disposable? = null

    // Makes GraphQL call through Apollo and threads it using RxJava then sets the Live Data
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

    // For MainActivity, add a Recent Search to room's database
    fun addRecentSearch(recentSearch: RecentSearch){
        App.repository.addRecentSearch(recentSearch)
    }

    // Disposes of the disposable to prevent memory leak
    override fun onCleared() {
        super.onCleared()
        if (disposable != null){
            disposable?.dispose()
        }
    }
}