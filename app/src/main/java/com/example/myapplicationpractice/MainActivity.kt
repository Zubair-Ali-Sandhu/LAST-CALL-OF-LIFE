package com.example.myapplicationpractice.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.myapplicationpractice.R
import com.example.myapplicationpractice.navigation.NavKeys
import com.google.android.material.bottomnavigation.BottomNavigationView

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
    }
}