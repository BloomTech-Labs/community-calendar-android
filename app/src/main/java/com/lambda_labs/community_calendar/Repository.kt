package com.lambda_labs.community_calendar

import EventsByLocationQuery
import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room.databaseBuilder
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.apollo.ApolloClient
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.room.RecentSearchDatabase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class Repository(app: App) {

    /*
        Room
    */

    private val recentSearchDatabase by lazy {
        databaseBuilder(app, RecentSearchDatabase::class.java, "recent_search_db").build()
    }

    fun getRecentSearchList(): Flowable<MutableList<Search>>{
        return recentSearchDatabase.dataAccessObject().getRecentSearchList()
    }

    fun addRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().addRecentSearch(search)
    }

    fun updateRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().updateRecentSearch(search)
    }

    fun removeRecentSearch(search: Search){
        recentSearchDatabase.dataAccessObject().removeRecentSearch(search)
    }


    /*
        Apollo
     */

    private val _events = MutableLiveData<List<EventsQuery.Event>>()
    val events: LiveData<List<EventsQuery.Event>> = _events

    // Makes GraphQL call through Apollo and threads it using RxJava then sets the Live Data
    fun getEvents(): Disposable {
        return ApolloClient.client().rxQuery(EventsQuery())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith (object: DisposableObserver<Response<EventsQuery.Data>>() {
                override fun onComplete() {

                }

                override fun onNext(t: Response<EventsQuery.Data>) {
                    _events.value  = t.data()?.events()
                }

                override fun onError(e: Throwable) {
                    _events.value = ArrayList()
                    println(e.message)
                }

            })

    }

    private val _locationEvents = MutableLiveData<List<EventsByLocationQuery.Event>>()
    val locationEvents: LiveData<List<EventsByLocationQuery.Event>> = _locationEvents

    fun getEventsByLocation(latitude: Double, longitude: Double): Disposable {
        return ApolloClient.client().rxQuery(EventsByLocationQuery(latitude, longitude))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<Response<EventsByLocationQuery.Data>>(){
                override fun onComplete() {  }

                override fun onNext(response: Response<EventsByLocationQuery.Data>) {
                    val sortedList = response.data()?.events()?.sortedBy { it.locations()?.get(0)?.distanceFromUser() }
                    _locationEvents.value = sortedList
                }

                override fun onError(e: Throwable) {
                    _locationEvents.value = ArrayList()
                }
            })
    }
}