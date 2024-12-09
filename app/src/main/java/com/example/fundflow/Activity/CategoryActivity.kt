package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityCategorySearchBinding
import com.example.fundflow.fragment.KategoriFragmentRec
import com.example.fundflow.fragment.KategoriFragmentRec2
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
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            if (fragment is KategoriFragmentRec || fragment is KategoriFragmentRec2) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Tutup activity
            } else {
                super.onBackPressed() // Default behavior
            }
        }

        // Fungsi Tombol Tambah Kategori
        binding.tambahKategori.setOnClickListener {
            // Pindah ke aktivitas untuk menambah kategori baru
            val intent = Intent(this, TambahCategoryActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Initialize TabLayout
        tabLayout = findViewById(R.id.tabLayout)

        // Set up the TabLayout with tabs
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

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
                .replace(R.id.fragmentContainer, it, it.javaClass.simpleName) // Menambahkan tag fragment
                .commit()
        }
    }

    // Fungsi untuk menyegarkan fragment yang sesuai dengan tab aktif
    fun refreshCategoryFragment() {
        val currentTab = tabLayout.selectedTabPosition
        val fragmentTag = when (currentTab) {
            0 -> KategoriFragmentRec::class.java.simpleName
            1 -> KategoriFragmentRec2::class.java.simpleName
            else -> null
        }

        fragmentTag?.let { tag ->
            val fragment = supportFragmentManager.findFragmentByTag(tag)
            if (fragment is RefreshableFragment) {
                fragment.refreshCategories() // Panggil metode refresh pada fragment aktif
            } else {
                // Jika fragment tidak ditemukan, buat ulang
                val newFragment = when (currentTab) {
                    0 -> KategoriFragmentRec()
                    1 -> KategoriFragmentRec2()
                    else -> null
                }
                newFragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, it, tag)
                        .commit()
                }
            }
        }
    }
}

// Tambahkan interface untuk mendukung fungsi refresh pada fragment
interface RefreshableFragment {
    fun refreshCategories()
}