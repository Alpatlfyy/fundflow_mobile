package com.example.fundflow.Activity

import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.example.fundflow.R
import com.example.fundflow.fragment.KategoriFragmentKeluar
import com.example.fundflow.fragment.KategoriFragmentMasuk
import com.example.fundflow.fragment.PengeluaranFragment
import com.google.android.material.tabs.TabLayout

private lateinit var viewPager: ViewPager2
lateinit var tabLayout: TabLayout
lateinit var fragmentContainer: FrameLayout
private lateinit var imageViewBulat: ImageView

class TambahCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_category)

        imageViewBulat = findViewById(R.id.iconKategori)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.addTab(tabLayout.newTab().setText("Pemasukan"))
        tabLayout.addTab(tabLayout.newTab().setText("Pengeluaran"))

        fragmentContainer = findViewById(R.id.fragmentContainer)
        displayFragment(0)

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
            0 -> KategoriFragmentMasuk(this) // Kirim activity
            1 -> KategoriFragmentKeluar(this)
            else -> null
        }
        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, it)
                .commit()
        }
    }

    // Metode untuk mengganti gambar ic_bulat berdasarkan resource ID
    fun changeImage(resourceId: Int) {
        imageViewBulat.setImageResource(resourceId)
    }
}
