package com.lambda_labs.community_calendar.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.adapter.RecentSearchRecyclerChild
import com.lambda_labs.community_calendar.model.Search
import com.lambda_labs.community_calendar.util.hideKeyboard
import com.lambda_labs.community_calendar.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.searches_recycler_item.view.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

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
        mainActivity.apply {
            btn_cancel.visibility = View.GONE
            search_bar.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            val constraintSetHide = ConstraintSet()
            constraintSetHide.clone(c_layout)
            constraintSetHide.connect(search_bar.id, ConstraintSet.END, c_layout.id, ConstraintSet.END)
            constraintSetHide.applyTo(c_layout)
            hideKeyboard(this)
            search_bar.clearFocus()
            search_bar.setQuery("", false)
        }
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

            val image = holder.drop

            holder.searchText.text = recentSearch.searchText

// Uncomment code below to test and change the recentSearch call in line 115 and 117 to fakeSearch
            /*val tags = arrayListOf("Java", "Kotlin", "Android") as List<String>
            val fakeSearch = Search("bob", "Utah", 84601, getToday(), tags)*/

            val list = viewModel.searchToSearchList(recentSearch)

            val hasNoFilters = viewModel.hasNoFilters(recentSearch)
            if (hasNoFilters) holder.drop.visibility = View.GONE

            var clicked = 0
            image.setOnClickListener {
                val layoutManager = LinearLayoutManager(activity)
                holder.recyclerView.layoutManager = layoutManager
                holder.recyclerView.adapter = RecentSearchRecyclerChild(list)
                if (clicked == 0) {
                    holder.recyclerView.visibility = View.VISIBLE
                    image.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.drop_up))
                    clicked = 1
                }
                else {
                    holder.recyclerView.visibility = View.GONE
                    image.setImageDrawable(ContextCompat.getDrawable(mainActivity, R.drawable.drop_down))
                    clicked = 0
                }
            }
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val searchText: TextView = view.search_txt
            val drop: ImageView = view.drop_down
            val recyclerView: RecyclerView = view.recycler_recent_search

        }
    }
}
