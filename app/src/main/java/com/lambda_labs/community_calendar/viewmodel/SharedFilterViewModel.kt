package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lambda_labs.community_calendar.model.Filter

class SharedFilterViewModel {
    private val sharedFilterData: MutableLiveData<Filter> = MutableLiveData()

    fun getSharedData(): LiveData<Filter> {
        return sharedFilterData
    }

    fun setSharedData(filter: Filter) {
        sharedFilterData.value = filter
    }
}