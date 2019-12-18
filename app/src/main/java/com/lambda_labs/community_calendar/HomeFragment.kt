package com.lambda_labs.community_calendar


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.event_recycler_item.view.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var strings: ArrayList<String> = arrayListOf("Strings", "The Stuff", "Run", "Strings", "The Stuff", "Run")
        strings.add("asdf")
        
        main_event_recycler.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        main_event_recycler.adapter = EventRecycler(strings)
    }

    inner class EventRecycler(private val events: ArrayList<String>) :
        RecyclerView.Adapter<EventRecycler.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.event_recycler_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = events.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = events[position]

            holder.title.text = event
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.random_txt
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
