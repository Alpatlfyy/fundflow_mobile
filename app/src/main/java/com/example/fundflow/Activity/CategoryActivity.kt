package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityCategorySearchBinding  // Import binding class
import com.example.fundflow.fragment.KategoriFragmentRec
import com.example.fundflow.fragment.KategoriFragmentRec2
import com.example.fundflow.fragment.PemasukanFragment
import com.example.fundflow.fragment.PengeluaranFragment
import com.google.android.material.tabs.TabLayout

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategorySearchBinding  // Deklarasi binding

    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

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
            val intent = Intent(this, TambahCategoryActivity::class.java)
            startActivity(intent)
        }
        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayout)

        // Set up the TabLayout with tabs
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        // Initialize Fragment Container
        // Initialize Fragment Container
        fragmentContainer = findViewById(R.id.fragmentContainer)

        // Display PemasukanFragment when the activity is created
        displayFragment(0)

        // Set listener on tab selected
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                displayFragment(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun displayFragment(position: Int) {
        val fragment = when (position) {
            0 -> KategoriFragmentRec() // Ganti dengan fragment Pemasukan
            1 -> KategoriFragmentRec2() // Ganti dengan fragment Pengeluaran
            else -> null
        }
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }
    }
}

