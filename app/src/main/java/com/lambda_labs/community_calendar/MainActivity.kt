package com.lambda_labs.community_calendar

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lambda_labs.community_calendar.viewmodel.HomeViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Startup navigation component
        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = host.navController
        setupBottomNavMenu(navController)

        val viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.getEvents()

        // Store the display attributes of the current screen in the static variable
        displayMetrics = applicationContext.resources.displayMetrics
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    // Setup bottom navigation bar
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }

    companion object {
        // Store the screen attributes to be accessible elsewhere
        lateinit var displayMetrics: DisplayMetrics
    }
}
