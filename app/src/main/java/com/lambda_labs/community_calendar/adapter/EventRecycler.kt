package com.lambda_labs.community_calendar.adapter

import EventsQuery
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.JsonUtil.eventJsonKey
import com.lambda_labs.community_calendar.util.JsonUtil.eventToJson
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
        event.eventImages()?.let {
            if (it.size > 0) {
                Picasso.get().load(event.eventImages()?.get(0)?.url()).into(holder.eventImage)
            }
        }
        holder.eventName.text = event.title()
        holder.eventTime.text = displayTime(event.start(), event.end())
        event.locations()?.let {
            if (it.size > 0) {
                holder.eventCommunity.text = event.locations()?.get(0)?.name()
            }
        }

        holder.cardView.setOnClickListener {
            val bundle = Bundle()
            val eventJson = eventToJson(event)
            bundle.putString(eventJsonKey(), eventJson)
            it.findNavController().navigate(R.id.eventDetailsFragment, bundle)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Change the variables from the grid's layout or list's layout variables depending up which is selected
        val cardView: CardView = if (isGridViewSelected) {
            view.card_view_grid
        } else {
            view.card_view
        }

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