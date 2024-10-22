package com.example.fundflow.Activity

import ExpenseListAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.R
import com.example.fundflow.ViewModel.MainViewModel
import com.example.fundflow.databinding.ActivityCashflowBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CashFlowActivity : ComponentActivity() {
    lateinit var binding: ActivityCashflowBinding
    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCashflowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        initRecycleview()

        // Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        // Tombol Back
        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "dashboard")
            startActivity(intent)
            finish() // Tutup CashFlowActivity setelah kembali
        }

        val btncatat = findViewById<Button>(R.id.btncatat)
        // Menangani klik pada tombol login
        btncatat.setOnClickListener {
            Log.d("", "Catat button clicked")
            val intent = Intent(this, ArusKasActivity::class.java)
            startActivity(intent)
        }
    }



    private fun initRecycleview() {
        binding.view1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.view1.adapter = ExpenseListAdapter(mainViewModel.loadData())
    }
}
