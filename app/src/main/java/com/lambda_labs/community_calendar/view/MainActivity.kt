package com.lambda_labs.community_calendar.view

import EventsQuery
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lambda_labs.community_calendar.R
import com.lambda_labs.community_calendar.util.hideKeyboard
import com.lambda_labs.community_calendar.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()

    private val mainModule: Module = module {
        single { SearchView(this@MainActivity) }
    }

    //    Setup bottom navigation bar
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setup app for searchBar
        loadKoinModules(mainModule)

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
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null){
                    val bundle = Bundle()
                    bundle.putDouble("Latitude", it.latitude)
                    bundle.putDouble("Longitude", it.longitude)
                    bundle.putString("search", "Location")
                    hideKeyboard(this)
                    findNavController(R.id.nav_host_fragment).navigate(R.id.searchResultFragment, bundle)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}