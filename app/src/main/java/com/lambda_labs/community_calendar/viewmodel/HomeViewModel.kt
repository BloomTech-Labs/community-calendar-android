package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.rxQuery
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.MainActivity
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.apollo.ApolloClient.client
import com.lambda_labs.community_calendar.model.RecentSearch
import com.lambda_labs.community_calendar.util.hideKeyboard
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
    private var searchDisposable: Disposable? = null

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
    private fun addRecentSearch(recentSearch: RecentSearch){
        searchDisposable = Schedulers.io().createWorker().schedule {
            App.repository.addRecentSearch(recentSearch)
        }
    }

    // Search actions
    fun addSearchesToDatabase(searchView: SearchView){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    // Function connects to repository (see above function)
                    addRecentSearch(RecentSearch(query))
                }
                hideKeyboard(searchView.context as MainActivity)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    // Disposes of the disposable to prevent memory leak
    override fun onCleared() {
        if (disposable != null){
            disposable?.dispose()
        }
        if (searchDisposable != null){
            searchDisposable?.dispose()
        }
        super.onCleared()
    }


    // TODO: Separate viewmodels. Below is for HomeFragment Specific

    fun setupRecyclers(orientation: Int, activity: FragmentActivity?, recycler: RecyclerView){
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, orientation, false)
    }

    fun selectListView(mainActivity: MainActivity, gridBtn: ImageView, listBtn: ImageView){
        listBtn.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.list_view_selected))
        gridBtn.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.grid_view_unselected))
    }

    fun selectGridView(mainActivity: MainActivity, gridBtn: ImageView, listBtn: ImageView){
        listBtn.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.list_view_unselected))
        gridBtn.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.grid_view_selected))
    }


}