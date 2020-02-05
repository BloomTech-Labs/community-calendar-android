package com.lambda_labs.community_calendar.viewmodel

import androidx.lifecycle.ViewModel
import com.lambda_labs.community_calendar.Repository

class LoginViewModel(private val repo: Repository): ViewModel() {
    fun saveToken(string: String?) {
        repo.saveToken(string)
    }

    fun getToken(): String?{
        return repo.token
    }
}