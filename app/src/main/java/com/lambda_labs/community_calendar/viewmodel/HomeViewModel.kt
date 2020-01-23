package com.lambda_labs.community_calendar.viewmodel

import EventsQuery
import android.util.TypedValue
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.lambda_labs.community_calendar.Repository

class HomeViewModel(val repo: Repository): ViewModel() {



    fun getAllEvents(): LiveData<List<EventsQuery.Event>> {
        return repo.events
    }

    fun setupRecyclers(orientation: Int, activity: FragmentActivity?, recycler: RecyclerView) {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(activity, orientation, false)
    }

    fun setupSearchBarConstraints(parent: ConstraintLayout, search: SearchView, materialText: MaterialTextView){
        val dpToPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, parent.context.resources.displayMetrics).toInt()

        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(search.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(search.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(search.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(materialText.id, ConstraintSet.TOP, search.id, ConstraintSet.BOTTOM, dpToPx)
        constraintSet.applyTo(parent)
    }
}