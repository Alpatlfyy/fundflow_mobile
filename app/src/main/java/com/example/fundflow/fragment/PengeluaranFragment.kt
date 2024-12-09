package com.example.fundflow.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.* // Untuk penggunaan Date

class PengeluaranFragment : Fragment() {
    val currentUid = UserSingleton.getUid()
    private lateinit var categorySpinner: Spinner
    private lateinit var circleIconView: ImageView
    private lateinit var amountEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var addButton: Button
    private lateinit var firestore: FirebaseFirestore
    private val categories = mutableListOf<Category>() // List untuk kategori

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengeluaran, container, false)

        categorySpinner = view.findViewById(R.id.userRoleSpinner)
        circleIconView = view.findViewById(R.id.category_circle_icon)
        amountEditText = view.findViewById(R.id.et_nominal)
        notesEditText = view.findViewById(R.id.et_tulis_catatan)
        addButton = view.findViewById(R.id.btnTambahKategori)
        firestore = FirebaseFirestore.getInstance()


        // Ambil data kategori dari Firestore
        fetchCategories { fetchedCategories ->
            categories.clear() // Kosongkan list kategori yang lama
            categories.addAll(fetchedCategories) // Tambahkan kategori yang baru
            val categoryNames = categories.map { it.name } // Ambil nama kategori untuk spinner

            // Membuat ArrayAdapter untuk spinner
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }

        // Listener untuk Spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                val iconResId = if (!selectedCategory.icon.isNullOrEmpty()) {
                    resources.getIdentifier(
                        selectedCategory.icon,
                        "drawable",
                        requireContext().packageName
                    )
                } else {
                    0
                }

                if (iconResId != 0) {
                    circleIconView.setImageResource(iconResId)
                } else {
                    circleIconView.setImageResource(R.drawable.ic_bc_profil)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                circleIconView.setImageResource(R.drawable.ic_bc_profil)
            }
        }

        // Listener untuk tombol tambah
        addButton.setOnClickListener {
            addArusKasToFirestore()
        }

        return view
    }

    private fun addArusKasToFirestore() {
        val selectedCategory = categorySpinner.selectedItem.toString()
        val selectedCategoryIcon = categories.find { it.name == selectedCategory }?.icon // Cari ikon kategori
        val amount = amountEditText.text.toString().toDoubleOrNull()
        val notes = notesEditText.text.toString()

        val userid = currentUid

        // Validasi input
        if (amount == null || amount <= 0) {
            Log.e("PengeluaranFragment", "Nominal tidak valid")
            return
        }

        val arusKasData = hashMapOf(
            "kategori" to selectedCategory,
            "jumlah" to amount,
            "catatan" to notes,
            "icon" to selectedCategoryIcon, // Tambahkan properti ikon ke Firestore
            "tanggal" to Date(), // Gunakan tanggal sekarang
            "jenis" to "pengeluaran", // Tetapkan jenis arus kas
            "userid" to userid
        )

        firestore.collection("aruskas")
            .add(arusKasData)
            .addOnSuccessListener { documentReference ->
                // Menampilkan Toast setelah data berhasil ditambahkan
                Toast.makeText(requireContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                // Clear form input setelah berhasil
                clearForm()

                // Kembali ke CashFlowActivity atau Activity sebelumnya
                requireActivity().onBackPressed() // Atau bisa menggunakan findNavController().navigateUp() jika menggunakan Navigation Component
            }
            .addOnFailureListener { exception ->
                Log.e("PengeluaranFragment", "Error menambahkan data: ", exception)
                Toast.makeText(requireContext(), "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
            }
    }

    // Fungsi untuk mengambil data kategori dari Firestore
    private fun fetchCategories(callback: (List<Category>) -> Unit) {
        firestore.collection("kategori")
            .whereEqualTo("userid",currentUid)
            .whereEqualTo("type", "Pengeluaran") // Ganti dengan "Pemasukan" atau "pemasukan" sesuai database
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    Log.e("PengeluaranFragment", "No categories found for type: Pemasukan")
                    callback(emptyList()) // Callback daftar kosong jika tidak ada data
                } else {
                    val fetchedCategories = querySnapshot.documents.map { document ->
                        Category(
                            name = document.getString("name") ?: "Unknown", // Nama kategori
                            icon = document.getString("icon")               // Nama ikon
                        )
                    }
                    Log.d("PengeluaranFragment", "Fetched categories: $fetchedCategories")
                    callback(fetchedCategories) // Callback dengan data
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PengeluaranFragment", "Error fetching categories: ", exception)
                callback(emptyList()) // Callback daftar kosong jika gagal
            }
    }

    // Fungsi untuk meng-clear form setelah data berhasil ditambahkan
    private fun clearForm() {
        amountEditText.text.clear()
        notesEditText.text.clear()
        categorySpinner.setSelection(0) // Reset spinner ke item pertama
        circleIconView.setImageResource(R.drawable.ic_bc_profil) // Set ikon default
    }

    // Kelas data untuk kategori
    data class Category(
        val name: String,        // Nama kategori
        val icon: String?    // Nama file ikon di drawable (nullable)
    )
}
