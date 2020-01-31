package com.lambda_labs.community_calendar

import EventsByLocationQuery
import EventsQuery
import RSVPMutation
import UserQuery
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room.databaseBuilder
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.rxMutate
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.apollo.ApolloClient
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.room.RecentSearchDatabase
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class Repository(val app: App) {

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

    // Takes in user auth token and the id of an event and adds event to user rsvp's
    fun rsvpForEvent(token: String, eventId: String): Single<Response<RSVPMutation.Data>> {
        return ApolloClient.authClient(token).rxMutate(RSVPMutation(eventId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


    }

    private val _user = MutableLiveData<UserQuery.User>()
    val user: LiveData<UserQuery.User> = _user

    // Helps us get user for the sake of seeing if they have previously rsvp'd an event when clicked
    // Or helps setup profile page
    fun getUser(token: String): Disposable{
        return ApolloClient.authClient(token).rxQuery(UserQuery())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<Response<UserQuery.Data>>(){
                override fun onComplete() {}

                override fun onNext(t: Response<UserQuery.Data>) {
                    _user.value = t.data()?.user()
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    lateinit var sharedPrefs: SharedPreferences
    private val tokenKey = "token_key"
    // For the sake of using
    var token: String? = null

    // So user can stay logged in if they exit the app and re-enter (Tokens expire in an hour)
    // Function should only ever be used before a user manually logs in to check for token
    fun getOldToken(): String?{
        sharedPrefs = app.getSharedPreferences("Token", Context.MODE_PRIVATE)
        token = sharedPrefs.getString(tokenKey, "")
        return token
    }

    // Saves the token when the user logs in
    fun saveToken(userToken: String?){
        token = userToken
        if (userToken != null){
            sharedPrefs = app.getSharedPreferences("Token", Context.MODE_PRIVATE)
            sharedPrefs.edit().putString(userToken, tokenKey).apply()
        }
    }
}