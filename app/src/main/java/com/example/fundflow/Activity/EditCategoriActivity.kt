package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.fundflow.R

import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

class EditCategoriActivity : AppCompatActivity() {

    private lateinit var namaCategory: EditText
    private lateinit var iconCategory: ImageView
    private lateinit var nameCategory: TextView
    private lateinit var iconnCategory: ImageView
    private lateinit var typeCategory: TextView
    private lateinit var firestore: FirebaseFirestore  // Tambahkan Firebase Firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        // Ganti warna Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getColor(R.color.blue_00549C)
        }

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

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
        typeCategory = findViewById(R.id.typeCategory)

        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        val categoryIcon = intent.getIntExtra("CATEGORY_ICON", R.drawable.ic_def_rapat)
        val categoryType = intent.getStringExtra("CATEGORY_TYPE")

        // Menampilkan data di UI
        categoryName?.let {
            namaCategory.setText(it)
            nameCategory.setText(it)
        }

        Glide.with(this).load(categoryIcon).into(iconCategory)
        Glide.with(this).load(categoryIcon).into(iconnCategory)

        categoryType?.let {
            typeCategory.text = it
            typeCategory.visibility = View.VISIBLE
        }

        // Tombol Edit dan Simpan
        val editButton = findViewById<Button>(R.id.btnSave)
        editButton.setOnClickListener {
            val newName = namaCategory.text.toString().trim()

            if (newName.isEmpty()) {
                Toast.makeText(this, "Nama kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cari dokumen berdasarkan nama kategori
            firestore.collection("kategori")
                .whereEqualTo("name", categoryName) // Mencari dokumen berdasarkan field "name"
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val documentId =
                            querySnapshot.documents[0].id // Dapatkan ID dokumen pertama

                        // Perbarui nama kategori menggunakan ID dokumen
                        firestore.collection("kategori").document(documentId)
                            .update("name", newName)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Kategori berhasil diperbarui",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, CategoryActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Gagal memperbarui kategori: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                e.printStackTrace() // Debug error
                            }
                    } else {
                        Toast.makeText(this, "Kategori tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mencari kategori: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }
    }