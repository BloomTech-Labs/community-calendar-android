package com.lambda_labs.community_calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.getRecentSearchDisplay
import com.lambda_labs.community_calendar.util.recentSearchDisplayText
import kotlinx.android.synthetic.main.recent_search_item.view.*
import java.util.*

// This is used to display a list of filters on the recent searches
class RecentSearchRecyclerChild(val list: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // 0 is even 1 is odd
        return position % 2
    }

    // inflates layouts depending on getItemViewType
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        return when(viewType){
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.searches_recycler_item_divider, parent, false)
                ViewHolder2(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_search_item, parent, false)
                ViewHolder(view)
            }
        }
    }

    // doubles list size to add both the lines and search items
    override fun getItemCount() = list.size * 2

    // adds line layout on even(0) and on odd adds filter item
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 1) {
            holder as ViewHolder
            var type = "by "
            var item = list[position / 2]
            when (item) {
                is Date -> {
                    item = getRecentSearchDisplay(item)
                    type += "date"
                }
                is List<*> -> {
                    item = item.joinToString(", ")
                    type += "tag"
                }
                is Int -> {
                    item = item.toString()
                    type += "area code"
                }
                else -> {
                    item = item.toString()
                    type += "location"
                }
            }

            val display = recentSearchDisplayText(type, item)

            holder.textView.text = display
        }
    }

    // This is search item
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val textView: MaterialButton = view.recent_search_txt

    }

    // This is the line
    class ViewHolder2(val view: View): RecyclerView.ViewHolder(view)
}