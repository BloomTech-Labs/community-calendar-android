package com.lambda_labs.community_calendar.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

        // Handles logic for how to display btn_read_more,
        txt_event_details.post {
            val defaultDescriptionLineCount = 2
            var withinDefaultLineCount = defaultDescriptionLineCount >= txt_event_details.lineCount
            if (withinDefaultLineCount)
                btn_read_more.visibility = View.GONE
            txt_event_details.maxLines = defaultDescriptionLineCount
            btn_read_more.setOnClickListener {
                val readSelection = it as MaterialButton
                if (withinDefaultLineCount){
                    txt_event_details.maxLines = defaultDescriptionLineCount
                    withinDefaultLineCount = false
                    readSelection.text = getString(R.string.read_more)
                }else {
                    txt_event_details.maxLines = Integer.MAX_VALUE
                    withinDefaultLineCount = true
                    readSelection.text = getString(R.string.read_less)
                }
            }
        }

        // Sets the attend button to correct text if user previously RSVP'd
        val userToken = viewModel.getToken()
        val eventId = event?.id()


        if (userToken != null){
            viewModel.startUserRetrieval(userToken)
            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                user.rsvps()?.forEach {rsvp ->
                    if (rsvp.title() == event?.title())
                    {
                        btn_attend.text = getString(R.string.unattend)
                        btn_calendar.visibility = View.VISIBLE
                    }
                }
            })
        }


        viewModel.isRsvp().observe(viewLifecycleOwner, Observer { rsvpd ->
            val text = if (rsvpd) getString(R.string.unattend) else getString(R.string.attend)
            btn_calendar.visibility = if (rsvpd) View.VISIBLE else View.GONE
            btn_attend.text = text
            pb_details.visibility = View.GONE
            details_layout.overlay.clear()
        })


        btn_attend.setOnClickListener {

            if (!eventId.isNullOrEmpty() && !userToken.isNullOrEmpty()) {
                viewModel.rsvpForEvent(userToken, eventId)
                val dim = ColorDrawable(Color.BLACK)
                dim.setBounds(0,0,details_layout.width, details_layout.height)
                dim.alpha = 177
                pb_details.visibility = View.VISIBLE
                details_layout.overlay.add(dim)
                details_layout.isClickable = false

            }
            else if (userToken.isNullOrEmpty()) {
                Toast.makeText(it.context, "Please Login :)", Toast.LENGTH_SHORT).show()
            }
        }

        btn_map.setOnClickListener {
            val intent = viewModel.locationIntent(event)
            activity?.let {
                if (intent.resolveActivity(it.packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        btn_calendar.setOnClickListener {
            val intent = viewModel.calenderIntent(event)
            activity?.let {
                if (intent.resolveActivity(it.packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }
}
