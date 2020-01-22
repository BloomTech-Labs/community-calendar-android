package com.lambda_labs.community_calendar.view

import EventsQuery
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()

    val mainModule: Module = module {
        single { SearchView(this@MainActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setup app for searchBar and cancel button
        loadKoinModules(mainModule)
        val searchView: SearchView = get()

//        Startup navigation component
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = host.navController
        setupBottomNavMenu(navController)

        viewModel.queryEvents()
        val events = ArrayList<EventsQuery.Event>()
        viewModel.getAllEvents().observe(this, Observer<List<EventsQuery.Event>> { list ->
            list.forEach { event ->
                events.add(event)
                println(event.title())
            }
        })

        // Checks to see if search bar was selected and navigates accordingly
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus && navController.currentDestination?.id != R.id.searchFragment){
                navController.navigate(R.id.searchFragment)
            }
        }

        // Adds searches to room db
        viewModel.searchNSave(searchView, events)
    }

//    Setup bottom navigation bar
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }
}