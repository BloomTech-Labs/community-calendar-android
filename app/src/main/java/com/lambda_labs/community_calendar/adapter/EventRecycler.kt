package com.lambda_labs.community_calendar.adapter

import EventsQuery
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.displayTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_recycler_item_grid.view.*
import kotlinx.android.synthetic.main.event_recycler_item_list.view.*

//    Recycler for General Events in HomeFragment and for Events in SearchResultFragment
class EventRecycler(
    private val events: ArrayList<EventsQuery.Event>,
    private val isGridViewSelected: Boolean
) :
    RecyclerView.Adapter<EventRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        // Switch which layout is inflated based on whether list or grid view is selected
        val view: View = if (isGridViewSelected) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_recycler_item_grid, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_recycler_item_list, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        event.event_images()?.let {
            if (it.size > 0) {
                Picasso.get().load(event.event_images()?.get(0)?.url()).into(holder.eventImage)
            }
        }
        holder.eventName.text = event.title()
        holder.eventTime.text = displayTime(event.start(), event.end())
        event.locations()?.let {
            if (it.size > 0) {
                holder.eventCommunity.text = event.locations()?.get(0)?.name()
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Change the variables from the grid's layout or list's layout variables depending up which is selected
        val eventImage: ImageView = if (isGridViewSelected) {
            view.img_event_grid
        } else {
            view.img_event
        }
        val eventName: MaterialTextView = if (isGridViewSelected) {
            view.txt_event_name_grid
        } else {
            view.txt_event_name
        }
        val eventTime: MaterialTextView = if (isGridViewSelected) {
            view.txt_event_time_grid
        } else {
            view.txt_event_time
        }
        val eventCommunity: MaterialTextView = if (isGridViewSelected) {
            view.txt_community_grid
        } else {
            view.txt_community
        }
    }
}