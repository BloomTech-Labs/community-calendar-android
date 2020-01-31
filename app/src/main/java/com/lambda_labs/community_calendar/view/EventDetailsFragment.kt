package com.lambda_labs.community_calendar.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.*
import com.lambda_labs.community_calendar.util.JsonUtil.eventJsonKey
import com.lambda_labs.community_calendar.util.JsonUtil.jsonToEvent
import com.lambda_labs.community_calendar.viewmodel.EventDetailsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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
        val eventId = event?.id()


        if (userToken != null){
            viewModel.startUserRetrieval(userToken)
            viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
                user.rsvps()?.forEach {rsvp ->
                    if (rsvp.title() == event?.title())
                    {
                        btn_attend.text = getString(R.string.attend)
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

            }
            else if (userToken.isNullOrEmpty()) {
                Toast.makeText(it.context, "Please Login :)", Toast.LENGTH_SHORT).show()
            }
        }

        btn_map.setOnClickListener {
            // Pass event location to external maps app

            // Get the values from the selected event details
            val encodedLatitude= Uri.encode(event?.locations()?.get(0)?.latitude().toString())
            val encodedLongitude= Uri.encode(event?.locations()?.get(0)?.longitude().toString())
            val encodedLocationName= Uri.encode(event?.locations()?.get(0)?.name())

            // By Name
            //val uriMaps: Uri = Uri.parse("geo:0,0?z=17&q=$encodedLocationName")

            // By Coordinates
            val uriMaps: Uri = Uri.parse("geo:0,0?q=$encodedLatitude,$encodedLongitude($encodedLocationName)&z=17")

            // Create the Intent object to have the OS decide how to show the place on the Map
            val intent: Intent = Intent(Intent.ACTION_VIEW,uriMaps)
            startActivity(intent)
        }

        btn_calendar.setOnClickListener {
            // Pass the event date and time to external calendar app

            // Convert the times from a String to a Date to a Calendar and into epoch milliseconds

            // Start time
            var timeToString: String =event?.start().toString()
            var stringToDate: Date = stringToDate(timeToString)
            var dateToCalendar: Calendar = toCalendar(stringToDate)
            val startTimeMilliseconds: Long =dateToCalendar.timeInMillis

            // End time
            timeToString =event?.end().toString()
            stringToDate = stringToDate(timeToString)
            dateToCalendar = toCalendar(stringToDate)
            val endTimeMilliseconds: Long =dateToCalendar.timeInMillis

            // Extract the event information for the calendar entry
            val eventName=event?.title()
            val eventDescription=event?.description()
            val eventLatitude=event?.locations()?.get(0)?.latitude()
            val eventLongitude=event?.locations()?.get(0)?.longitude()
            val eventCoordinates = "$eventLatitude,$eventLongitude"

            // Create the Intent object with extras of event info to launch externally
            val intent:Intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMilliseconds)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMilliseconds)
                .putExtra(CalendarContract.Events.TITLE, eventName)
                .putExtra(CalendarContract.Events.DESCRIPTION, eventDescription)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, eventCoordinates)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
            startActivity(intent)
        }
    }
}
