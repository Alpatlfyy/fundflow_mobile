package com.example.fundflow.Activity

import ExpenseListAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewmodel.compose.viewModel
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.R
import com.example.fundflow.ViewModel.MainViewModel
import com.example.fundflow.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

// Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.white_dsbd) // Mengatur warna status bar ke putih

            // Mengatur ikon dan teks status bar menjadi gelap
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, // Ikon dan teks gelap
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS // Mengatur flag
                )
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Untuk versi Android sebelum R
            }
        }




        // Inisialisasi FAB
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val invoiceBox = findViewById<LinearLayout>(R.id.anggota)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        // Set onClickListener untuk FAB
        fab.setOnClickListener {
            // Navigasi ke CashflowActivity
            val intent = Intent(this, CashFlowActivity::class.java)
            startActivity(intent)
        }

        invoiceBox.setOnClickListener {
            // Navigasi ke InvoiceListActivity (Compose Activity)
            val intent = Intent(this, InvoiceListActivity::class.java)
            startActivity(intent)
        }
        val catatan = findViewById<LinearLayout>(R.id.catatan)

        catatan.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Handle home action
                    true
                }
                R.id.activity -> {
                    // Handle arus kas action
                    val intent = Intent(this, CategoryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.report -> {
                    // Handle report action
                    val intent = Intent(this, reportActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
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