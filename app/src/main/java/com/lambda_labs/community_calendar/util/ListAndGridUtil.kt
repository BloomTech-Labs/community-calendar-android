package com.lambda_labs.community_calendar.util

import EventsQuery
import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.adapter.EventRecycler

// Use for toggling buttons and toggling a recycler view from list view to grid view and vise versa

fun selectListView(
    recyclerView: RecyclerView, list: ArrayList<EventsQuery.Event>,
    context: Context, gridBtn: ImageView, listBtn: ImageView
) {
    listBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.list_view_selected))
    gridBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.grid_view_unselected))
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.adapter = EventRecycler(list, false)
}

fun selectGridView(
    recyclerView: RecyclerView, list: ArrayList<EventsQuery.Event>,
    context: Context, gridBtn: ImageView, listBtn: ImageView
) {

    listBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.list_view_unselected))
    gridBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.grid_view_selected))
    recyclerView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    recyclerView.adapter = EventRecycler(list, true)
}