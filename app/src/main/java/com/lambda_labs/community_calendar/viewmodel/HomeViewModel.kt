package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda_labs.community_calendar.App

class HomeViewModel: ViewModel() {

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return App.repository.events
    }

    fun setupRecyclers(orientation: Int, activity: FragmentActivity?, recycler: RecyclerView) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, orientation, false)
    }


}