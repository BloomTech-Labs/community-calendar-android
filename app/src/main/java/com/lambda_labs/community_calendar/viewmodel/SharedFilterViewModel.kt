package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.model.Filter

class SharedFilterViewModel : ViewModel() {
    private val sharedFilterData: MutableLiveData<Filter> = MutableLiveData<Filter>()

    fun getSharedData(): LiveData<Filter> {
        return sharedFilterData
    }

    fun setSharedData(filter: Filter) {
        sharedFilterData.value = filter
    }
}