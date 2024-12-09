package com.example.fundflow.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.databinding.ActivityEditAruskasBinding
import com.google.firebase.firestore.FirebaseFirestore

// Pastikan hanya ada satu data class Category
data class Category(val name: String, val icon: String)

class EditArusKasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAruskasBinding
    private val db = FirebaseFirestore.getInstance()
    private var categories =
        mutableListOf<Category>() // Menyimpan kategori yang diambil dari Firestore
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAruskasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data yang dikirimkan melalui Intent untuk mengedit kategori
        documentId = intent.getStringExtra("DOCUMENT_ID")
        val jenisArusKas = intent.getStringExtra("CATEGORY_JENIS")
        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        val categoryIcon = intent.getStringExtra("CATEGORY_ICON")
        val categoryType = intent.getStringExtra("CATEGORY_TYPE") // Pemasukan / Pengeluaran
        val categoryNominal = intent.getDoubleExtra("CATEGORY_NOMINAL", 0.0)
        val categoryJenis =
            intent.getStringExtra("CATEGORY_JENIS") // Jenis: Pemasukan / Pengeluaran dari aruskas

        // Tampilkan data yang didapatkan di UI
        categoryName?.let {
            binding.categoryNameEditText.setText(it)
        }
        categoryType?.let {
            binding.tvJenis.text = "Tipe: $it"
        }
        binding.amountEditText.setText(categoryNominal.toString())

        // Set ikon berdasarkan icon yang didapatkan
        categoryIcon?.let {
            val iconResId = resources.getIdentifier(it, "drawable", packageName)
            if (iconResId != 0) {
                binding.iconImageView.setImageResource(iconResId)
            } else {
                binding.iconImageView.setImageResource(R.drawable.ic_bc_profil) // Default icon jika tidak ditemukan
            }
        }

        // Ambil kategori berdasarkan jenis
        if (jenisArusKas != null) {
            fetchCategoriesBasedOnJenis(jenisArusKas)
        } else {
            Toast.makeText(this, "Jenis arus kas tidak tersedia", Toast.LENGTH_SHORT).show()
        }

        // Tombol Simpan
        binding.btnSave.setOnClickListener {
            saveCategory(jenisArusKas ?: "")
        }
    }

    // Fungsi untuk mengambil data kategori dari Firestore berdasarkan tipe
    private fun fetchCategoriesBasedOnJenis(jenis: String) {
        Log.d("FETCH_CATEGORIES", "Fetching categories for jenis: $jenis")
        val typeToQuery =
            if (jenis.equals("Pemasukan", ignoreCase = true)) "Pemasukan" else "Pengeluaran"

        db.collection("kategori")
            .whereEqualTo("type", typeToQuery)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Toast.makeText(
                        this,
                        "Tidak ada kategori ditemukan untuk jenis: $jenis",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("FETCH_CATEGORIES", "Tidak ada kategori ditemukan untuk jenis: $jenis")
                } else {
                    categories = querySnapshot.documents.map { document ->
                        Category(
                            name = document.getString("name") ?: "Unknown",
                            icon = document.getString("icon") ?: ""
                        )
                    }.toMutableList()

                    Log.d("FETCH_CATEGORIES", "Kategori ditemukan: ${categories.size}")
                    categories.forEach {
                        Log.d("FETCH_CATEGORIES", "Kategori: ${it.name}, Icon: ${it.icon}")
                    }

                    // Set data ke Spinner
                    val categoryNames = categories.map { it.name }
                    val adapter =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.categoryTypeSpinner.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error fetching categories: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("FETCH_CATEGORIES", "Error: ${exception.message}")
            }
    }

    // Fungsi untuk menyimpan kategori setelah editing
    private fun saveCategory(jenis: String) {
        if (documentId.isNullOrEmpty()) {
            Toast.makeText(this, "ID dokumen tidak ditemukan, tidak bisa menyimpan.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCategory = binding.categoryTypeSpinner.selectedItem.toString()
        val amount = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0

        if (selectedCategory.isEmpty() || amount <= 0) {
            Toast.makeText(this, "Mohon isi semua data dengan benar.", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedData = mapOf(
            "kategori" to selectedCategory,
            "jenis" to jenis,
            "jumlah" to amount
        )

        db.collection("aruskas")
            .document(documentId!!)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil diperbarui.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal memperbarui data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}