package com.lambda_labs.community_calendar.view

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.dpToPx
import com.lambda_labs.community_calendar.util.hideKeyboard
import com.lambda_labs.community_calendar.util.setSearchBarProperties
import com.lambda_labs.community_calendar.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.searches_recycler_item.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: SearchViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    private val searchBar: SearchView by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()

        if (searchBar.parent != null){
            (searchBar.parent as ViewGroup).removeView(searchBar)
        }
        search_layout.addView(searchBar)
        setSearchBarProperties(searchBar, false, topMargin = 0f, endMargin = 0f)
        searchBar.background = ContextCompat.getDrawable(mainActivity, R.drawable.small_search_box)

       viewModel.setupSearchBarConstraints(search_layout, searchBar, btn_cancel, btn_filters)

        // Navigates out of SearchFragment to previous fragment.
        // onDestroy has more logic to wrap this action up.
        btn_cancel.setOnClickListener {
            findNavController().navigateUp()
        }


        searches_recycler.setHasFixedSize(true)
        searches_recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        viewModel.getRecentSearches()
        viewModel.searchList.observe(viewLifecycleOwner, Observer<MutableList<Search>> { recentSearches ->
            searches_recycler.adapter = RecentSearchRecycler(recentSearches)
        })


        // Uncomment to test how it displays the chips
        /*val fake = this.context as Context

        val listTest = arrayListOf("Test","Test1","Test2","Test3","Test4","Test5","Test6")
        createChipLayout(listTest, fake, chip_group_search)
        chips.visibility = View.VISIBLE*/


        btn_filters.setOnClickListener {
            hideKeyboard(mainActivity)
            this.findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }



    }

    override fun onDestroy() {
        hideKeyboard(mainActivity)
        searchBar.clearFocus()
        searchBar.setQuery("", false)

        super.onDestroy()
    }

    inner class RecentSearchRecycler(private val searches: MutableList<Search>) :
        RecyclerView.Adapter<RecentSearchRecycler.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.searches_recycler_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = searches.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val recentSearch = searches[position]

            holder.searchText.text = recentSearch.searchText
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val searchText: TextView = view.search_txt
        }
    }
}
