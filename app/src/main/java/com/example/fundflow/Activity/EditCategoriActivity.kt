package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fundflow.R

class EditCategoriActivity : AppCompatActivity() {

    private lateinit var namaCategory: EditText
    private lateinit var iconCategory: ImageView
    private lateinit var nameCategory: TextView
    private lateinit var iconnCategory: ImageView
    private lateinit var typeCategory: TextView  // Tambahkan ini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        // Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        // Tombol Back
        val backButton = findViewById<ImageButton>(R.id.btnnnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            finish() // Tutup CashFlowActivity setelah kembali
        }

        // Inisialisasi komponen
        namaCategory = findViewById(R.id.namaCategory)
        iconCategory = findViewById(R.id.iconKategori)
        nameCategory = findViewById(R.id.nameCategory)
        iconnCategory = findViewById(R.id.iconnKategori)
        typeCategory = findViewById(R.id.typeCategory)  // Inisialisasi TextView tipe kategori

        // Ambil data dari Intent
        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        val categoryIcon = intent.getIntExtra("CATEGORY_ICON", R.drawable.ic_def_rapat)
        val categoryType = intent.getStringExtra("CATEGORY_TYPE")  // Ambil tipe kategori

        // Menampilkan nama kategori ke kedua EditText
        categoryName?.let {
            namaCategory.setText(it)
            nameCategory.setText(it)
        }

        // Menampilkan ikon kategori ke kedua ImageView secara terpisah
        Glide.with(this)
            .load(categoryIcon) // Menggunakan Glide untuk memuat gambar ke icon pertama
            .into(iconCategory)  // Memuat gambar ke icon pertama

        Glide.with(this)
            .load(categoryIcon) // Menggunakan Glide untuk memuat gambar ke icon kedua
            .into(iconnCategory) // Memuat gambar ke icon kedua

        // Menampilkan tipe kategori di TextView
        if (categoryType != null) {
            typeCategory.text = categoryType  // Set text menjadi "Pemasukan" atau "Pengeluaran"
            typeCategory.visibility = View.VISIBLE  // Tampilkan TextView

            // Menampilkan tipe kategori di TextView
            if (categoryType != null) {
                typeCategory.text = categoryType
            }
        }
    }
}
