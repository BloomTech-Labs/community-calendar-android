package com.lambda_labs.community_calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lambda_labs.community_calendar.model.RecentSearch
import com.lambda_labs.community_calendar.viewmodel.HomeViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity() {

    lateinit var disposable: Disposable

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

        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val action = R.id.searchFragment
                navController.navigate(action)
                if (query != null){
                    disposable = Schedulers.io().createWorker().schedule {
                        viewModel.addRecentSearch(RecentSearch(query))
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

//    Setup bottom navigation bar
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.setupWithNavController(navController)
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
