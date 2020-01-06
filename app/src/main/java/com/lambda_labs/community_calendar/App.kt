package com.lambda_labs.community_calendar

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App: Application() {
    companion object{
        const val TOKEN_KEY = "token_key"
        lateinit var sharedPrefs: SharedPreferences
        var token: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences("Token", Context.MODE_PRIVATE)
        token = sharedPrefs.getString(TOKEN_KEY, "")
    }
}