package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda_labs.community_calendar.App
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.view.MainActivity

class HomeViewModel: ViewModel() {

    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return App.repository.events
    }

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