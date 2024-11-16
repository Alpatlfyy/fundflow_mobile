package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityNoteBinding
import com.example.fundflow.fragment.NoteFragmentRec

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengatur warna Status Bar jika versi SDK mendukung
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        // Inisialisasi binding
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Fungsi Tombol Kembali
        binding.btnnBack.setOnClickListener {
            finish()  // Menutup aktivitas saat tombol kembali ditekan
        }

        // Fungsi FloatingActionButton untuk membuka AddNoteActivity
        binding.tambahkecatatan.setOnClickListener {
            val intent = Intent(this, NoteAddActivity::class.java)
            startActivity(intent)
        }

        // Menampilkan fragment NoteFragmentRec saat aktivitas dibuat
        displayFragment(NoteFragmentRec())
    }

    // Fungsi untuk menampilkan fragment dalam fragmentContainer
    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}