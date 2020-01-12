package com.lambda_labs.community_calendar


import EventsQuery
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.util.Util.displayTime
import com.lambda_labs.community_calendar.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.event_recycler_item_grid.view.*
import kotlinx.android.synthetic.main.event_recycler_item_list.view.*
import kotlinx.android.synthetic.main.featured_event_recycler_item.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var mainActivity: Context
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val options = navOptions {
            anim {
                enter = R.anim.slide_bottom_up
                exit = R.anim.stagnant
                popEnter = R.anim.stagnant
                popExit = R.anim.slide_bottom_down
            }
        }

        if (txt_see_all != null) {
            txt_see_all.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_home_to_filterFragment, null, options)
            }
        }





        // ViewModel
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


//        Dummy data for recycler views
        val strings: ArrayList<String> =
            arrayListOf("Strings", "The Stuff", "Run", "Strings", "The Stuff", "Run")
        strings.add("asdf")

        // event list
        val events = ArrayList<EventsQuery.Event>()

//        Setup Featured event recycler
        featured_event_recycler.setHasFixedSize(true)
        featured_event_recycler.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        featured_event_recycler.adapter = FeaturedEventRecycler(strings)

//        Setup General events recycler view in list view format
        main_event_recycler.setHasFixedSize(true)
        main_event_recycler.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        main_event_recycler.adapter = EventRecycler(events, false)

//        Buttons switch user between List View and Grid View, change to light and dark version of images based on view selection
        btn_grid.setOnClickListener {
            btn_grid.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.grid_view_selected
                )
            )
            btn_list.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.list_view_unselected
                )
            )
            main_event_recycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            main_event_recycler.adapter = EventRecycler(events, true)
        }

        btn_list.setOnClickListener {
            btn_grid.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.grid_view_unselected
                )
            )
            btn_list.setImageDrawable(
                ContextCompat.getDrawable(
                    mainActivity,
                    R.drawable.list_view_selected
                )
            )
            main_event_recycler.layoutManager =
                LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            main_event_recycler.adapter = EventRecycler(events, false)
        }
        // Network call through HomeViewMode
        viewModel.getEvents()
        viewModel.events.observe(viewLifecycleOwner, Observer<List<EventsQuery.Event>> { list ->
            list.forEach { event ->
                events.add(event)
            }
            main_event_recycler.adapter?.notifyDataSetChanged()
            pb_events.visibility = View.INVISIBLE
        })
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
    inner class EventRecycler(
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
            //Picasso.get().load(event.event_images()?.get(0)?.url()).into(holder.eventImage)
            holder.eventName.text = event.title()
            holder.eventTime.text = displayTime(event.start(), event.end())
            //holder.eventCommunity.text = event.locations()?.get(0)?.name()

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

    //    Setup a way to directly call MainActivity's context for changing button highlighted in grid and list views
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
    }
}
