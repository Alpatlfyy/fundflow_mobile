package com.example.fundflow.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Handle home action
                    true
                }
                R.id.nav_arus_kas -> {
                    // Handle arus kas action
                    true
                }
                R.id.nav_report -> {
                    // Handle report action
                    true
                }
                R.id.nav_profile -> {
                    // Handle profile action
                    true
                }
                else -> false
            }
        }

    }
}