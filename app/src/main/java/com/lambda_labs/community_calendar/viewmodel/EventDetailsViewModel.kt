package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import UserQuery
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository
import com.lambda_labs.community_calendar.util.stringToDate
import io.reactivex.disposables.Disposable

class EventDetailsViewModel(private val repo: Repository): ViewModel() {

    private var disposable: Disposable? = null
    private var userDisposable: Disposable? = null
    private val _isRsvp = MutableLiveData<Boolean>()
    private val isRsvp: LiveData<Boolean> = _isRsvp

    fun rsvpForEvent(token: String, eventId: String){
        disposable = repo.rsvpForEvent(token, eventId)
            .subscribe(
            {_isRsvp.value = it.data()?.rsvpEvent()},
            {println(it.message)}
        )
    }

    fun isRsvp(): LiveData<Boolean> {
        return isRsvp
    }

    fun setupSearchBarConstraints(parent: ConstraintLayout, search: SearchView, materialText: View){
        val dpToPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, parent.context.resources.displayMetrics).toInt()

        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(search.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(search.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(search.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(materialText.id, ConstraintSet.TOP, search.id, ConstraintSet.BOTTOM, dpToPx)
        constraintSet.applyTo(parent)
    }

    fun getToken(): String?{
        return repo.token
    }

    fun startUserRetrieval(token: String) {
        userDisposable = repo.getUser(token)
    }

    fun getUser(): LiveData<UserQuery.User>{
        return repo.user
    }

    fun locationIntent(event: EventsQuery.Event?): Intent {
        // Pass event location to external maps app

        // Get the values from the selected event details
        val location = event?.locations()?.get(0)
        val lat = location?.latitude()
        val long = location?.longitude()
        val name = location?.name()

        // By Coordinates
        val uriMaps: Uri = Uri.parse("geo:0,0?q=$lat, $long($name)&z=17")

        // Create the Intent object to have the OS decide how to show the place on the Map
        return Intent(Intent.ACTION_VIEW, uriMaps)
    }

    fun calenderIntent(event: EventsQuery.Event?): Intent {
        // Pass the event date and time to external calendar app

        // Convert the times from a String to a Date to a Calendar and into epoch milliseconds

        // Start time
        val startTimeMilliseconds = stringToDate(event?.start().toString()).time

        // End time
        val endTimeMilliseconds: Long = stringToDate(event?.end().toString()).time

        // Extract the event information for the calendar entry
        val eventName= event?.title()
        val eventDescription= event?.description()

        val location = event?.locations()?.get(0)
        val city = "${location?.city()}, ${location?.state()}, ${location?.zipcode()}"
        val address = "${location?.streetAddress()}, $city"

        // Create the Intent object with extras of event info to launch externally
        return Intent(Intent.ACTION_INSERT).apply {
            data = Events.CONTENT_URI
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTimeMilliseconds)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTimeMilliseconds)
            putExtra(Events.TITLE, eventName)
            putExtra(Events.DESCRIPTION, eventDescription)
            putExtra(Events.EVENT_LOCATION, address)
            putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        userDisposable?.dispose()
    }

}