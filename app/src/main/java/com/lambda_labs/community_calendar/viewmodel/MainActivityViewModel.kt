package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.Repository
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.hideKeyboard
import com.lambda_labs.community_calendar.util.searchEvents
import com.lambda_labs.community_calendar.view.MainActivity
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivityViewModel(val repo: Repository) : ViewModel() {

    // Creates LiveData to be observed on the HomeFragment


    // Init a global variable to be able to call it from another function
    private var disposable: Disposable? = null
    private var searchDisposable: Disposable? = null



    // For MainActivity, add a Recent Search to room's database
    fun addRecentSearch(search: Search){
        searchDisposable = Schedulers.io().createWorker().schedule {
            repo.addRecentSearch(search)
        }
    }

    fun queryEvents(){
        disposable = repo.getEvents()
    }

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return repo.events
    }

    // Search actions
    fun searchNSave(searchView: SearchView, events: ArrayList<EventsQuery.Event>){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    // Function connects to repository (see above function)
                    val userSearch = Search(query)
                    searchEvents(events, userSearch).forEach {
                        println(it.title())
                    }
                    addRecentSearch(userSearch)

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






}