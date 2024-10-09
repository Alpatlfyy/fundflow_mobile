package com.example.fundflow.Activity

import ExpenseListAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewmodel.compose.viewModel
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.R
import com.example.fundflow.ViewModel.MainViewModel
import com.example.fundflow.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    private val mainViewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDashboardBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)




//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )

        initRecycleview()

//        // Ganti warna Status Bar
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = getColor(R.color.white)
//        }




        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Handle home action
                    true
                }
                R.id.activity -> {
                    // Handle arus kas action
                    true
                }
                R.id.report -> {
                    // Handle report action
                    true
                }
                R.id.profile -> {
                    // Handle profile action
                    true
                }
                else -> false
            }
        }

    }

    private fun initRecycleview() {
       binding.view1.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.view1.adapter=ExpenseListAdapter(mainViewModel.loadData())
    }
}