package com.lambda_labs.community_calendar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.*
import com.lambda_labs.community_calendar.util.JsonUtil.eventJsonKey
import com.lambda_labs.community_calendar.util.JsonUtil.jsonToEvent
import com.lambda_labs.community_calendar.viewmodel.EventDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventDetailsFragment : Fragment() {

    private val viewModel: EventDetailsViewModel by viewModel()
    private val searchBar: CustomSearchView by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (searchBar.parent != null){
            (searchBar.parent as ViewGroup).removeView(searchBar)
        }

        details_layout.addView(searchBar)
        setSearchBarProperties(searchBar, true)
        searchBar.id = View.generateViewId()
        viewModel.setupSearchBarConstraints(details_layout, searchBar, event_image)


        val jsonEvent = arguments?.getString(eventJsonKey(), "") ?: ""

        val event = jsonToEvent(jsonEvent)

        Picasso.get().load(event?.eventImages()?.get(0)?.url()).into(event_image)
        event_title.text = event?.title()
        val startTime = event?.start().toString()
        val endTime = event?.end().toString()
        event_date.text = getDisplayDay((stringToDate(startTime)))
        event_address.text = event?.locations()?.get(0)?.streetAddress()
        val creator = event?.creator()
        Picasso.get().load(creator?.profileImage()).into(img_host)
        txt_host.text = creator?.firstName()
        txt_event_time.text = displayTime(startTime, endTime).replace(" ", "\n")
        val price = event?.ticketPrice()
        // String.format("$%.2f", price)
        txt_ticket_type.text = if (price == 0.0) "Free" else "Paid"
        txt_event_details.text = event?.description()

        // Sets the attend button to correct text if user previously RSVP'd
        val userToken = viewModel.getToken()
        if (userToken != null){
            viewModel.startUserRetrieval(userToken)
            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                user.rsvps()?.forEach {rsvp ->
                    if (rsvp.title() == event?.title()) btn_attend.text = "Unattend"
                }
            })
        }


        btn_attend.setOnClickListener {
            val eventId = event?.id()
            if (!eventId.isNullOrEmpty() && !userToken.isNullOrEmpty()) {
                val rsvp = viewModel.rsvpForEvent(userToken, eventId)
                rsvp.observe(viewLifecycleOwner, Observer { rsvpd ->
                    val text = if (rsvpd) "Hello there" else "General Kenobi"
                    Toast.makeText(it.context, text, Toast.LENGTH_SHORT).show()
                    it as MaterialButton
                    it.text = text
                })
            }
            else if (userToken.isNullOrEmpty()) {
                Toast.makeText(it.context, "Please Login :)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
