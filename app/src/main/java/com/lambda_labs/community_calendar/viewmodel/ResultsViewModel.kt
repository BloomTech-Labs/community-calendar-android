package com.lambda_labs.community_calendar.viewmodel

import EventsByLocationQuery
import EventsQuery
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository
import io.reactivex.disposables.Disposable

class ResultsViewModel(private val repo: Repository): ViewModel() {
    private var disposable: Disposable? = null

    fun getEventsByLocation(latitude: Double, longitude: Double) {
        disposable = repo.getEventsByLocation(latitude, longitude)
    }

    fun getLiveDataEventListByLocation(): LiveData<List<EventsByLocationQuery.Event>> {
        return repo.locationEvents
    }

    fun convertQuery(eventsByLocation: List<EventsByLocationQuery.Event>): ArrayList<EventsQuery.Event>{
        val eventsList = ArrayList<EventsQuery.Event>()
        eventsByLocation.forEach {
            val eventImageList = ArrayList<EventsQuery.EventImage>()
            val locationsList = ArrayList<EventsQuery.Location>()
            val tagsList = ArrayList<EventsQuery.Tag>()
            it.eventImages()?.forEach { image ->
                val eventImage = EventsQuery.EventImage(image.__typename(), image.url())
                eventImageList.add(eventImage)
            }
            it.locations()?.forEach { loc ->
                val eventLocation = EventsQuery.Location(loc.__typename(), loc.distanceFromUser(), loc.name(), loc.streetAddress(), loc.state(), loc.city(), loc.zipcode(), loc.latitude(), loc.longitude())
                locationsList.add(eventLocation)
            }
            it.tags()?.forEach { aTag ->
                val eventTag = EventsQuery.Tag(aTag.__typename(), aTag.title())
                tagsList.add(eventTag)
            }
            val newEvent = EventsQuery.Event(
                it.__typename(),
                it.id(),
                it.title(),
                it.description(),
                it.start(),
                it.end(),
                it.ticketPrice(),
                EventsQuery.Creator(it.creator()?.__typename() ?: "User", it.creator()?.profileImage() ,it.creator()?.firstName()),
                eventImageList,
                locationsList,
                tagsList
            )
            eventsList.add(newEvent)
        }
        return eventsList
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null){
            disposable?.dispose()
        }
    }
}