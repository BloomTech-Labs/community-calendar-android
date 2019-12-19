package com.lambda_labs.community_calendar


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.event_recycler_item_grid.view.*
import kotlinx.android.synthetic.main.event_recycler_item_list.view.*
import kotlinx.android.synthetic.main.featured_event_recycler_item.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var mainActivity: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Dummy data for recycler views
        val strings: ArrayList<String> = arrayListOf("Strings", "The Stuff", "Run", "Strings", "The Stuff", "Run")
        strings.add("asdf")

//        Setup Featured event recycler
        featured_event_recycler.setHasFixedSize(true)
        featured_event_recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        featured_event_recycler.adapter = FeaturedEventRecycler(strings)

//        Setup General events recycler view in list view format
        main_event_recycler.setHasFixedSize(true)
        main_event_recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        main_event_recycler.adapter = EventRecycler(strings, false)

//        Buttons switch user between List View and Grid View, change to light and dark version of images based on view selection
        btn_grid.setOnClickListener {
            btn_grid.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.grid_view_selected))
            btn_list.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.list_view_unselected))
            main_event_recycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            main_event_recycler.adapter = EventRecycler(strings, true)
        }

        btn_list.setOnClickListener {
            btn_grid.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.grid_view_unselected))
            btn_list.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.list_view_selected))
            main_event_recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            main_event_recycler.adapter = EventRecycler(strings, false)
        }
    }

//    Recycler for Featured Events
    inner class FeaturedEventRecycler(private val events: ArrayList<String>) :
        RecyclerView.Adapter<FeaturedEventRecycler.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.featured_event_recycler_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = events.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = events[position]

            holder.title.text = event
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: MaterialTextView = view.txt_featured_name
        }
    }

//    Recycler for General Events
    inner class EventRecycler(private val events: ArrayList<String>, private val isGridViewSelected: Boolean) :
        RecyclerView.Adapter<EventRecycler.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View

            // Switch which layout is inflated based on whether list or grid view is selected
            view = if (isGridViewSelected){
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_recycler_item_grid, parent, false)
            }else{
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_recycler_item_list, parent, false)
            }
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = events.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = events[position]

            holder.eventName.text = event
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            // Change the variables from the grid's layout or list's layout variables depending up which is selected
            val eventName: MaterialTextView = if(isGridViewSelected){ view.txt_event_name_grid } else{ view.txt_event_name }
            val eventTime: MaterialTextView = if (isGridViewSelected){ view.txt_event_time_grid } else { view.txt_event_time }
            val eventCommunity: MaterialTextView = if (isGridViewSelected){ view.txt_community_grid } else { view.txt_community }
        }
    }

//    Setup a way to directly call MainActivity's context for changing button highlighted in grid and list views
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }
}
