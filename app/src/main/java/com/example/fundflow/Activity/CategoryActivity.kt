package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityCategorySearchBinding  // Import binding class
import com.example.fundflow.fragment.PemasukanFragment
import com.example.fundflow.fragment.PengeluaranFragment
import com.google.android.material.tabs.TabLayout

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategorySearchBinding  // Deklarasi binding

    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding
        binding = ActivityCategorySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Inisialisasi ToolbarS
        setSupportActionBar(binding.toolbar)

        // Fungsi Tombol Kembali
        binding.btnnBack.setOnClickListener {
            finish()  // Menutup aktivitas saat tombol kembali ditekan
        }
            // Fungsi Tombol Tambah Kategori
            binding.tambahKategori.setOnClickListener {
                // Pindah ke aktivitas untuk menambah kategori baru
                val intent = Intent(this@CategoryActivity, TambahCategoryActivity::class.java)
                startActivity(intent)
            }
        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayout)

        // Set up the TabLayout with tabs
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        // Initialize Fragment Container
}
    }
