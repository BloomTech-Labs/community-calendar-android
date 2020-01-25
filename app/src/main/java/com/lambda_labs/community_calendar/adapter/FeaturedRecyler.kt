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
import com.lambda_labs.community_calendar.util.getDisplayDay
import com.lambda_labs.community_calendar.util.stringToDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.featured_event_recycler_item.view.*

// Recycler for Featured Events in the HomeFragment
class FeaturedRecycler(private val events: ArrayList<EventsQuery.Event>) :
    RecyclerView.Adapter<FeaturedRecycler.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.featured_event_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event: EventsQuery.Event = events[position]

        event.eventImages()?.let {
            if (it.size > 0) {
                Picasso.get().load(event.eventImages()?.get(0)?.url()).into(holder.image)
            }
        }
        holder.date.text = getDisplayDay(stringToDate(event.start().toString()))
        holder.title.text = event.title()
        holder.time.text = displayTime(event.start(), event.end())
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.img_featured
        val date: MaterialTextView = view.txt_featured_date
        val title: MaterialTextView = view.txt_featured_name
        val time: MaterialTextView = view.txt_featured_time
    }
}

