package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundflow.Adapter.RecentOperationsAdapter
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
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
    val currentUid = UserSingleton.getUid()

    private var totalPemasukan: Double = 0.0
    private var totalPengeluaran: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        // Inisialisasi RecyclerView
        binding.rvRecentOperations.apply {
            adapter = recentOperationsAdapter
            layoutManager = LinearLayoutManager(this@DashboardActivity)
        }

        // Setup real-time listeners
        setupRealTimeListeners()

        // Slider setup
        setupSlider()

        // Floating Action Button untuk menambah cash flow
        binding.fab.setOnClickListener {
            val intent = Intent(this, CashFlowActivity::class.java)
            startActivity(intent)
        }

        // Bottom Navigation
        setupBottomNavigation()

        fetchAndDisplayUsername()

    }


    private fun fetchAndDisplayUsername() {
        val currentUid = UserSingleton.getUid() // Ambil UID pengguna saat ini
        if (currentUid.isNullOrEmpty()) {
            Log.w("DashboardActivity", "UID kosong atau null")
            return
        }

        // Ambil data pengguna dari Firestore berdasarkan UID
        db.collection("users") // Sesuaikan nama koleksi Anda di Firestore
            .document(currentUid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val username = document.getString("username") // Sesuaikan nama field
                    if (!username.isNullOrEmpty()) {
                        binding.tvProfileName.text = username // Update TextView dengan username
                    } else {
                        binding.tvProfileName.text = "Pengguna" // Default jika username kosong
                    }
                } else {
                    Log.w("DashboardActivity", "Dokumen pengguna tidak ditemukan")
                    binding.tvProfileName.text = "Pengguna"
                }
            }
            .addOnFailureListener { e ->
                Log.e("DashboardActivity", "Gagal mengambil data pengguna", e)
                binding.tvProfileName.text = "Error"
            }
    }


    private fun setupSlider() {
        binding.invoice.setOnClickListener {
            val intent = Intent(this, InvoiceListActivity::class.java)
            startActivity(intent)
        }
        binding.anggota.setOnClickListener {
            val intent = Intent(this, AnggotaActivity::class.java)
            startActivity(intent)
        }
        binding.catatan.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }
        binding.utang.setOnClickListener {
            val intent = Intent(this, UtangActivity::class.java)
            startActivity(intent)
        }
        binding.piutang.setOnClickListener {
            val intent = Intent(this, PiutangActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false

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
            .whereEqualTo("userid",currentUid)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("DashboardActivity", "Listen failed for aruskas.", e)
                    return@addSnapshotListener
                }

                totalPemasukan = 0.0
                totalPengeluaran = 0.0
                val recentData = mutableListOf<RecentOperation>()

                snapshots?.documentChanges?.forEach { change ->
                    if (change.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val doc = change.document
                        val jumlah = doc.getDouble("jumlah") ?: 0.0
                        val jenis = doc.getString("jenis") ?: "unknown"
                        val kategori = doc.getString("kategori") ?: "unknown"
                        val icon = doc.getString("icon") ?: "ic_default"
                        val tanggal = doc.getTimestamp("tanggal") ?: throw IllegalStateException("Invalid date")

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

                        if (jenis.equals("pemasukan", ignoreCase = true)) {
                            totalPemasukan += jumlah
                        } else if (jenis.equals("pengeluaran", ignoreCase = true)) {
                            totalPengeluaran += jumlah
                        }
                    }
                }

                if (recentData.isNotEmpty()) {
                    Log.d("DataUpdate", "Updating adapter with ${recentData.size} items")
                    recentOperationsAdapter.updateData(recentData)
                }

                val kasPerusahaan = totalPemasukan - totalPengeluaran
                binding.tvIncomeValue.text = formatCurrency(totalPemasukan)
                binding.tvExpenseValue.text = formatCurrency(totalPengeluaran)
                binding.tvBalanceAmount.text = formatCurrency(kasPerusahaan)
            }
    }

    private fun formatCurrency(value: Double): String {
        return String.format(Locale("id", "ID"), "Rp %,d", value.toLong())
    }

    // Manual addition functions for testing
//    fun onNewCategoryAdded(name: String, type: String, iconName: String) {
//        val newCategory = RecentOperation(
//            type = type,
//            name = name,
//            icon = resources.getIdentifier(iconName, "drawable", packageName),
//            timestamp = System.currentTimeMillis() / 1000 // Current time
//        )
//        addRecentOperation(newCategory)
//    }
//
//    fun onNewCashFlowAdded(name: String, type: String, iconName: String, amount: Double) {
//        val newCashFlow = RecentOperation(
//            type = type,
//            name = name,
//            icon = resources.getIdentifier(iconName, "drawable", packageName),
//            amount = amount,
//            date = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(Date()),
//            timestamp = System.currentTimeMillis() / 1000 // Current time
//        )
//        addRecentOperation(newCashFlow)
//    }
}