package com.lambda_labs.community_calendar.view

import EventsQuery
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.adapter.EventRecycler
import com.lambda_labs.community_calendar.adapter.FeaturedRecycler
import com.lambda_labs.community_calendar.util.*
import com.lambda_labs.community_calendar.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: HomeViewModel

    //    Setup a way to directly call MainActivity's context for changing button highlighted in grid and list views
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity


        val mainModule: Module = module {
            single { SearchView(mainActivity) }
        }

//        setup app for searchBar
        loadKoinModules(mainModule)

        viewModel = get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    val searchBar: SearchView by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Checks to see if search bar was selected and navigates accordingly
        searchBar.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus && findNavController().currentDestination?.id != R.id.searchFragment){
                findNavController().navigate(R.id.searchFragment)
            }
        }

        if (searchBar.parent != null){
            (searchBar.parent as ViewGroup).removeView(searchBar)
        }
        home_layout.addView(searchBar)
        setSearchBarProperties(searchBar, true)
        viewModel.setupSearchBarConstraints(home_layout, searchBar, txt_featured_title)

        txt_see_all.setOnClickListener {
            findNavController().navigate(R.id.searchResultFragment)
        }

        // Event list
        val events = ArrayList<EventsQuery.Event>()
        val featuredList=ArrayList<EventsQuery.Event>()
        val filterList = ArrayList<EventsQuery.Event>()

        // Checks to see if the filterList is empty then displays a message if empty
        fun isEmpty(message: String){
            if(filterList.isNullOrEmpty()) {
                txt_no_events.visibility = View.VISIBLE
                val displayText = "There are no events $message :("
                txt_no_events.text = displayText
            } else {
                txt_no_events.visibility = View.INVISIBLE
            }
        }

        // Checks to see if the featuredList is empty then displays a message if empty
        fun isFeaturedEmpty() {
            if (featuredList.isNullOrEmpty()) {
                txt_no_featured_events.visibility = View.VISIBLE
                val displayText = "There are no featured events :("
                txt_no_featured_events.text = displayText
            } else {
                txt_no_featured_events.visibility = View.INVISIBLE
            }
        }

        // Get the subset of events that are specified as being featured
        fun grabFeaturedEvents() {
            // Ensure a blank list
            featuredList.clear()

            // Temporarily mixing up the events randomly until a Feature flag has been set
            val shuffledEvents: MutableList<EventsQuery.Event> = events.toMutableList()
            shuffledEvents.shuffle()
            val max: Int = 10 // Maximum number of events to show
            var shuffledEventsCount: Int = shuffledEvents.size
            if (shuffledEventsCount > max) shuffledEventsCount = max
            for (i: Int in 0 until shuffledEventsCount) {
                featuredList.add(shuffledEvents[i])
            }
            featured_event_recycler.adapter?.notifyDataSetChanged()
        }

        // Used by today tab and tomorrow tab to remove some boiler plate code
        fun changeDay(date: Date) {
            filterList.clear()
            filterList.addAll(events.filter { it.start().toString().contains(getSearchDate(date)) })
            main_event_recycler.adapter?.notifyDataSetChanged()
            txt_event_date.text = getDisplayDay(date)
        }

        // Changes the font to bold of the event date tabs according to what tab was clicked
        fun changeColor(view: View) {
            view as MaterialTextView
            val eventDates = arrayListOf<MaterialTextView>(
                txt_events_today,
                txt_events_tomorrow,
                txt_events_this_weekend,
                txt_events_all_upcoming
            )
            eventDates.forEach {
                it.typeface = ResourcesCompat.getFont(mainActivity,
                    R.font.poppins_light
                )
            }
            view.typeface = ResourcesCompat.getFont(mainActivity,
                R.font.poppins_semi_bold
            )
        }

        // Today tab filters by today events
        txt_event_date.text = getDisplayDay(getToday())
        txt_events_today.setOnClickListener {
            changeColor(it)
            changeDay(getToday())
            isEmpty("today")
        }

        // Tomorrow tab filters by tomorrows events
        txt_events_tomorrow.setOnClickListener {
            changeColor(it)
            changeDay(getTomorrow())
            isEmpty("tomorrow")
        }

        // Weekend tab filters by events this weekend
        txt_events_this_weekend.setOnClickListener {
            changeColor(it)
            filterList.clear()
            getWeekendDates().forEach { date ->
                filterList.addAll(events.filter {
                    it.start().toString().contains(getSearchDate(date))
                })
            }
            isEmpty("this weekend")
            main_event_recycler.adapter?.notifyDataSetChanged()
            val displayWeekend =
                "${getDisplayDay(getWeekendDates()[0])} - ${getDisplayDay(getWeekendDates()[2])}"
            txt_event_date.text = displayWeekend
        }

        // All upcoming filters by dates after today
        txt_events_all_upcoming.setOnClickListener {
            changeColor(it)
            filterList.clear()
            filterList.addAll(events.filter {
                val eventDate = stringToDate(it.start().toString())
                eventDate.after(getToday())
            })
            isEmpty("upcoming")
            main_event_recycler.adapter?.notifyDataSetChanged()
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val yearText = "$year - ${year+1}"
            txt_event_date.text = yearText
        }

        // Network call through HomeViewModel
        viewModel.getAllEvents().observe(viewLifecycleOwner, Observer<List<EventsQuery.Event>> { list ->
            list.forEach { event ->
                events.add(event)
            }
            changeDay(getToday())
            isEmpty("today")
            main_event_recycler.adapter?.notifyDataSetChanged()
            pb_events.visibility = View.INVISIBLE

            grabFeaturedEvents()
            isFeaturedEmpty()
            featured_event_recycler.adapter?.notifyDataSetChanged()
            pb_featured_events.visibility = View.INVISIBLE
        })

        // Setup Featured events recycler view
        viewModel.setupRecyclers(RecyclerView.HORIZONTAL, activity, featured_event_recycler)
        featured_event_recycler.adapter = FeaturedRecycler(featuredList)

        // Setup General events recycler view in list view format
        viewModel.setupRecyclers(RecyclerView.VERTICAL, activity, main_event_recycler)
        main_event_recycler.adapter = EventRecycler(filterList, false)

        // Buttons switch user between List View and Grid View, change to light and dark version of images based on view selection
        btn_grid.setOnClickListener {
            selectGridView(main_event_recycler, events, mainActivity, btn_grid, btn_list)
        }

        btn_list.setOnClickListener {
            selectListView(main_event_recycler, events, mainActivity, btn_grid, btn_list)
        }
    }
}
