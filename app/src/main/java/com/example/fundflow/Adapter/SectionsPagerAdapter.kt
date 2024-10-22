package com.example.fundflow.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fundflow.fragment.PemasukanFragment
import com.example.fundflow.fragment.PengeluaranFragment

class SectionsPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // Menentukan jumlah total halaman dalam ViewPager
    override fun getItemCount(): Int {
        return 2 // Karena kita memiliki 2 tab (Pemasukan dan Pengeluaran)
    }

    // Mengembalikan fragment yang sesuai untuk posisi tertentu
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PemasukanFragment() // Ganti dengan fragment Pemasukan
            1 -> PengeluaranFragment() // Ganti dengan fragment Pengeluaran
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
