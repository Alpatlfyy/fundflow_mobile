package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.R
import com.example.fundflow.ViewModel.MainViewModel
import com.example.fundflow.databinding.ActivityCashflowBinding
import com.example.fundflow.fragment.ExpenseListFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CashFlowActivity :  AppCompatActivity() {
    lateinit var binding: ActivityCashflowBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var fragmentContainer1: FrameLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashflowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayFragment(0)

        // Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        // Tombol Back
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "dashboard")
            startActivity(intent)
            finish() // Tutup CashFlowActivity setelah kembali
        }

        // Tombol Catat
        binding.btncatat.setOnClickListener {
            Log.d("", "Catat button clicked")
            val intent = Intent(this, ArusKasActivity::class.java)
            startActivity(intent)
        }

        // Tampilkan ExpenseListFragment secara default
        displayFragment(0)
    }

    // Fungsi untuk menampilkan fragment
    private fun displayFragment(position: Int) {
        val fragment = when (position) {
            0 -> ExpenseListFragment() // Tampilkan ExpenseListFragment
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer1, it) // Ganti fragmentContainer dengan id dari FrameLayout
                .commit()
        }
    }
}
