package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewmodel.compose.viewModel
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.Adapter.RecentOperationsAdapter
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityDashboardBinding
import com.example.fundflow.model.RecentOperation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val recentOperationsAdapter: RecentOperationsAdapter by lazy {
        RecentOperationsAdapter(mutableListOf())
    }

    private val db = FirebaseFirestore.getInstance()
    private val recentOperationsCache = mutableListOf<RecentOperation>()

    private var totalPemasukan: Double = 0.0
    private var totalPengeluaran: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        // Setup RecyclerView for recent operations
        binding.rvRecentOperations.apply {
            adapter = recentOperationsAdapter
            layoutManager = LinearLayoutManager(this@DashboardActivity)
        }

        // Setup real-time listeners for categories and cash flows
        setupRealTimeListeners()

        // Inisialisasi FAB
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val invoiceBox = findViewById<LinearLayout>(R.id.anggota)

        // Bottom Navigation setup
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

        // Set onClickListener untuk FAB
        fab.setOnClickListener {
            // Navigasi ke CashflowActivity
            val intent = Intent(this, CashFlowActivity::class.java)
            startActivity(intent)
        }

        val catatan = findViewById<LinearLayout>(R.id.catatan)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> true
                R.id.kategori -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    true
                }
                R.id.report -> {
                    startActivity(Intent(this, reportActivity::class.java))
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRealTimeListeners() {
        db.collection("aruskas")
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("DashboardActivity", "Listen failed for aruskas.", e)
                    return@addSnapshotListener
                }

                totalPemasukan = 0.0
                totalPengeluaran = 0.0
                val recentData = mutableListOf<RecentOperation>() // Pastikan tipe data cocok

                snapshots?.documentChanges?.forEach { change ->
                    if (change.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val doc = change.document
                        val jumlah = doc.getDouble("jumlah") ?: 0.0
                        val jenis = doc.getString("jenis") ?: "unknown"
                        val kategori = doc.getString("kategori") ?: "unknown"
                        val icon = doc.getString("icon") ?: "ic_default"
                        val tanggal = doc.getTimestamp("tanggal") ?: throw IllegalStateException("Invalid date")

                        // Tambahkan data ke daftar
                        recentData.add(
                            RecentOperation(
                                type = jenis,
                                name = kategori,
                                icon = resources.getIdentifier(icon, "drawable", packageName),
                                amount = jumlah,
                                date = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(tanggal.toDate()),
                                timestamp = tanggal.seconds
                            )
                        )

                        // Hitung total
                        if (jenis.equals("pemasukan", ignoreCase = true)) {
                            totalPemasukan += jumlah
                        } else if (jenis.equals("pengeluaran", ignoreCase = true)) {
                            totalPengeluaran += jumlah
                        }
                    }
                }

                // Update RecyclerView jika data valid
                if (recentData.isNotEmpty()) {
                    Log.d("DataUpdate", "Updating adapter with ${recentData.size} items")
                    recentOperationsAdapter.updateData(recentData)
                } else {
                    Log.w("DataUpdate", "No data available to update adapter")
                }

                // Update UI
                val kasPerusahaan = totalPemasukan - totalPengeluaran
                binding.tvIncomeValue.text = formatCurrency(totalPemasukan)
                binding.tvExpenseValue.text = formatCurrency(totalPengeluaran)
                binding.tvBalanceAmount.text = formatCurrency(kasPerusahaan)
            }
    }

    private fun addRecentOperation(operation: RecentOperation) {
        // Add the new operation to the cache
        recentOperationsCache.add(0, operation)

        // Sort cache by timestamp and limit to 5 items
        recentOperationsCache.sortByDescending { it.timestamp }
        if (recentOperationsCache.size > 5) {
            recentOperationsCache.removeLast()
        }

        // Update RecyclerView
        recentOperationsAdapter.updateData(recentOperationsCache)

        // Debugging log
        Log.d("RecentOperations", "Updated List: $recentOperationsCache")
    }

    // Format the currency value for display
    private fun formatCurrency(value: Double): String {
        return String.format(Locale("id", "ID"), "Rp %,d", value.toLong())
    }

    // Manual addition functions for testing
    fun onNewCategoryAdded(name: String, type: String, iconName: String) {
        val newCategory = RecentOperation(
            type = type,
            name = name,
            icon = resources.getIdentifier(iconName, "drawable", packageName),
            timestamp = System.currentTimeMillis() / 1000 // Current time
        )
        addRecentOperation(newCategory)
    }

    private fun initRecycleview() {
        binding.view1.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.view1.adapter=ExpenseListAdapter(mainViewModel.loadData())
    }
}
