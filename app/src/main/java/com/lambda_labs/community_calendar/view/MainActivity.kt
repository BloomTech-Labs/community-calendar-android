package com.lambda_labs.community_calendar.view

import EventsQuery
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        Startup navigation component
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = host.navController
        setupBottomNavMenu(navController)

        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.queryEvents()
        val events = ArrayList<EventsQuery.Event>()
        viewModel.getAllEvents().observe(this, Observer<List<EventsQuery.Event>> { list ->
            list.forEach { event ->
                events.add(event)
                println(event.title())
            }
        })

        // Navigates out of SearchFragment to previous fragment.
        // SearchFragment onDestroy has more logic to wrap this action up.
        btn_cancel.setOnClickListener {
            navController.navigateUp()
        }

        // Checks to see if search bar was selected and navigates accordingly
        search_bar.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus && navController.currentDestination?.id != R.id.searchFragment){
                navController.navigate(R.id.searchFragment)

                btn_cancel.visibility = View.VISIBLE
                search_bar.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
                val constraintSetShow = ConstraintSet()
                constraintSetShow.clone(c_layout)
                constraintSetShow.connect(search_bar.id, ConstraintSet.END, btn_cancel.id, ConstraintSet.START)
                constraintSetShow.applyTo(c_layout)
            }
        }

        // Adds searches to room db
        viewModel.searchNSave(search_bar, events)
    }

//    Setup bottom navigation bar
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }
}