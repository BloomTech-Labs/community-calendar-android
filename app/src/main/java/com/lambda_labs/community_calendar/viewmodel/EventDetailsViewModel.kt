package com.lambda_labs.community_calendar.viewmodel

import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository
import io.reactivex.disposables.Disposable

class EventDetailsViewModel(private val repo: Repository): ViewModel() {

    private var disposable: Disposable? = null
    private var userDisposable: Disposable? = null
    private val _isRsvp = MutableLiveData<Boolean>()
    private val isRsvp: LiveData<Boolean> = _isRsvp

    fun rsvpForEvent(token: String, eventId: String) : LiveData<Boolean> {
        disposable = repo.rsvpForEvent(token, eventId)
            .subscribe(
            {_isRsvp.value = it.data()?.rsvpEvent()},
            {println(it.message)}
        )
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

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        userDisposable?.dispose()
    }

}