package com.lambda_labs.community_calendar.view


import EventsQuery
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.adapter.EventRecycler
import com.lambda_labs.community_calendar.util.recentSearchDisplayText
import com.lambda_labs.community_calendar.util.selectGridView
import com.lambda_labs.community_calendar.util.selectListView
import kotlinx.android.synthetic.main.fragment_search_result.*

@Suppress("UNCHECKED_CAST")
class SearchResultFragment : Fragment() {

    private lateinit var eventList: ArrayList<EventsQuery.Event>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result_back_button.setOnClickListener {
            findNavController().navigateUp()
        }

        // gets bundle from SearchFragment and sets them to correct variables
        if (arguments != null) {
            val filteredList = arguments?.getParcelableArrayList<Parcelable>("list")
                ?: ArrayList<EventsQuery.Event>()
            eventList = filteredList as ArrayList<EventsQuery.Event>
            val searched = arguments?.getString("search") ?: "N/A"
            txt_searched_by.text = recentSearchDisplayText("by", searched)
        } else eventList = ArrayList()


        result_recycler_view.layoutManager = LinearLayoutManager(view.context)
        result_recycler_view.adapter = EventRecycler(eventList, false)

        result_btn_list.setOnClickListener {
            selectListView(
                result_recycler_view, eventList, view.context, result_btn_grid, result_btn_list
            )
        }

        result_btn_grid.setOnClickListener {
            selectGridView(
                result_recycler_view, eventList, view.context, result_btn_grid, result_btn_list
            )
        }



    }

}
