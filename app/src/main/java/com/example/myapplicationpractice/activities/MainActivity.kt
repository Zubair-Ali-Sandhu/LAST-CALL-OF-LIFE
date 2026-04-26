package com.example.myapplicationpractice.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.navigation.NavKeys
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME = "extra_user_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val graph = navController.navInflater.inflate(R.navigation.nav_main)
        val startArgs = Bundle().apply {
            putString(NavKeys.ARG_USER_NAME, intent.getStringExtra(EXTRA_USER_NAME).orEmpty())
        }
        navController.setGraph(graph, startArgs)

        findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
            .setupWithNavController(navController)

        // Show the "I'm Alive" FAB only on the Home screen
        val fab = findViewById<ExtendedFloatingActionButton>(R.id.fab_alive_button)
        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar_layout)

        // Fragments that use their own toolbar — hide the activity AppBar for them
        val fragmentsWithOwnToolbar = setOf(
            R.id.contactsFragment,
            R.id.newsFeedFragment,
            R.id.vaultHomeFragment,
            R.id.settingsFragment,
            R.id.contactDetailFragment,
            R.id.addEditContactFragment,
            R.id.vaultModuleDetailFragment,
            R.id.alertDetailFragment,
            R.id.profileFragment,
            R.id.securitySettingsFragment,
            R.id.notificationSettingsFragment,
            R.id.vaultAuthFragment,
            R.id.vaultMedicalFragment,
            R.id.vaultAssetsFragment,
            R.id.vaultFinancesFragment,
            R.id.vaultEstateFragment,
            R.id.vaultThreatsFragment,
            R.id.vaultWishesFragment
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                fab.visibility = View.VISIBLE
                appBarLayout.visibility = View.VISIBLE
            } else {
                fab.visibility = View.GONE
                if (destination.id in fragmentsWithOwnToolbar) {
                    appBarLayout.visibility = View.GONE
                } else {
                    appBarLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}
