package com.lambda_labs.community_calendar

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.lambda_labs.community_calendar.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
import org.koin.dsl.module

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidContext(this@App)
            printLogger(Level.INFO /*Also DEBUG and ERROR Levels*/)
            modules(modules)
        }
    }

    // Starts Dependency Injection for app viewmodels
    val modules: Module = module {
        single { this@App }
        viewModel { FilterViewModel(get()) }
        viewModel { HomeViewModel(get()) }
        viewModel { SearchViewModel(get()) }
        viewModel { MainActivityViewModel(get()) }
        viewModel { ResultsViewModel(get()) }
        viewModel { EventDetailsViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        single { SharedFilterViewModel() }
        single { Repository(get()) }
    }
}
