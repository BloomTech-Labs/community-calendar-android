package com.lambda_labs.community_calendar

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

// Set this class as our Application class to initialize the user's authentication Token at startup
class App: Application() {
    companion object{
        const val TOKEN_KEY = "token_key"
        lateinit var sharedPrefs: SharedPreferences
        var token: String? = null

        lateinit var repository: Repository
    }

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences("Token", Context.MODE_PRIVATE)
        token = sharedPrefs.getString(TOKEN_KEY, "")

        repository = Repository(this)

    }
}