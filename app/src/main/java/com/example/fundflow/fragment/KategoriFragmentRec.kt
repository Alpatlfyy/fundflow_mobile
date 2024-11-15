package com.example.fundflow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.R
import com.example.fundflow.adapter.CategoryAdapter
import com.example.fundflow.model.CategoryItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class KategoriFragmentRec : Fragment() {
    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val categoryList = mutableListOf<CategoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycleview_category, container, false)

        // Initialize RecyclerView
        recyclerViewCategory = view.findViewById(R.id.view1)
        recyclerViewCategory.layoutManager = LinearLayoutManager(context)

        // Initialize Adapter
        recyclerViewCategory.layoutManager = LinearLayoutManager(requireContext())
        categoryAdapter = CategoryAdapter(requireContext(), categoryList, "Pemasukan") // atau "Pengeluaran"

        recyclerViewCategory.adapter = categoryAdapter

        // Load categories from Firestore
        loadCategoriesFromFirestore("Pemasukan") // change to "Pengeluaran" for different fragment if needed

        return view
    }

    private fun loadCategoriesFromFirestore(type: String) {
        println("Loading categories of type: $type")  // Debug log

        firestore.collection("kategori")
            .whereEqualTo("type", type)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val categoryList = mutableListOf<CategoryItem>()
                if (documents.isEmpty) {
                    println("No documents found for type: $type") // Jika tidak ada dokumen ditemukan
                    showEmptyState()
                } else {
                    for (document in documents) {
                        val categoryName = document.getString("name") ?: "Unknown"
                        val categoryIcon = document.getString("icon")
                        println("Found category: $categoryName with type: ${document.getString("type")}") // Debug log

                        categoryList.add(CategoryItem(categoryName, getIconDrawable(categoryIcon)))
                    }
                    categoryAdapter.updateCategories(categoryList)
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
                showErrorState()
            }
    }



    private fun showEmptyState() {
        // Display an empty state message or image (add logic here if needed)
        println("No categories available.")
    }

    private fun showErrorState() {
        // Display an error state message to the user (add logic here if needed)
        println("Failed to load categories.")
    }

    // Map icon string to drawable resource (assuming your icons are stored in resources)
    private fun getIconDrawable(iconName: String?): Int {
        return when (iconName) {
            "ic_rapat" -> R.drawable.ic_rapat
            "ic_helm" -> R.drawable.ic_helm
            "ic_kerjsama" -> R.drawable.ic_kerjsama
            "ic_daun" -> R.drawable.ic_daun
            "ic_dokumen" -> R.drawable.ic_dokumen
            "ic_kacapembesar" -> R.drawable.ic_kacapembesar
            "ic_catur" -> R.drawable.ic_catur
            "ic_mic" -> R.drawable.ic_mic
            "ic_perusahaan" -> R.drawable.ic_perusahaan
            "ic_rocket" -> R.drawable.ic_rocket
            "ic_lampu" -> R.drawable.ic_lampu
            "ic_book" -> R.drawable.ic_book
            "ic_tandatgn" -> R.drawable.ic_tandatgn
            "ic_catat" -> R.drawable.ic_catat
            "ic_pidato" -> R.drawable.ic_pidato
            "ic_bumi" -> R.drawable.ic_bumi
            "ic_medali" -> R.drawable.ic_medali
            "ic_lingkungan" -> R.drawable.ic_lingkungan
            "ic_mcd" -> R.drawable.ic_mcd
            "ic_kentang" -> R.drawable.ic_kentang
            "ic_ayam" -> R.drawable.ic_ayam
            "ic_rotitawar" -> R.drawable.ic_rotitawar
            "ic_mie" -> R.drawable.ic_mie
            "ic_elur" -> R.drawable.ic_elur
            "ic_nasi" -> R.drawable.ic_nasi
            "ic_kueultah" -> R.drawable.ic_kueultah
            "ic_ikan" -> R.drawable.ic_ikan
            "ic_daging" -> R.drawable.ic_daging
            "ic_gnja" -> R.drawable.ic_gnja
            "ic_apel" -> R.drawable.ic_apel
            "ic_teh" -> R.drawable.ic_teh
            "ic_es" -> R.drawable.ic_es
            "ic_kopi" -> R.drawable.ic_kopi
            "ic_botol" -> R.drawable.ic_botol
            "ic_cup" -> R.drawable.ic_cup
            "ic_shope" -> R.drawable.ic_shope
            "ic_gojek" -> R.drawable.ic_gojek
            "ic_grab" -> R.drawable.ic_grab
            "ic_flesdis" -> R.drawable.ic_flesdis
            "ic_kursi" -> R.drawable.ic_kursi
            "ic_pos" -> R.drawable.ic_pos
            "ic_komputer" -> R.drawable.ic_komputer
            "ic_pin" -> R.drawable.ic_pin
            "ic_lembaran" -> R.drawable.ic_lembaran
            "ic_kertaspnjg" -> R.drawable.ic_kertaspnjg
            "ic_peniti" -> R.drawable.ic_peniti
            "ic_telpun" -> R.drawable.ic_telpun
            "ic_print" -> R.drawable.ic_print
            "ic_mobil" -> R.drawable.ic_mobil
            "ic_sepeda" -> R.drawable.ic_sepeda
            "ic_bus" -> R.drawable.ic_bus
            "ic_helykopter" -> R.drawable.ic_helykopter
            "ic_truk" -> R.drawable.ic_truk
            "ic_motor" -> R.drawable.ic_motor
            "ic_kereta" -> R.drawable.ic_kereta
            "ic_kapal" -> R.drawable.ic_kapal
            "ic_jet" -> R.drawable.ic_jet
            "ic_pesawat" -> R.drawable.ic_pesawat
            else -> R.drawable.ic_bc_profil // Default icon if none match
        }
    }
}
