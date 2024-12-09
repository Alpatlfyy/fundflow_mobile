package com.example.fundflow.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.example.fundflow.adapter.CategoryAdapter
import com.example.fundflow.model.CategoryItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class KategoriFragmentRec : Fragment() {

    private lateinit var recyclerViewCategory: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var loadingIndicator: ProgressBar
    private val firestore = FirebaseFirestore.getInstance()
    private val categoryList = mutableListOf<CategoryItem>()
    var tabType: String = "Pemasukan" // Default type is "Pemasukan"
    val currentUid = UserSingleton.getUid()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycleview_category, container, false)

        recyclerViewCategory = view.findViewById(R.id.view1)
        recyclerViewCategory.layoutManager = LinearLayoutManager(context)

        categoryAdapter = CategoryAdapter(requireContext(), categoryList, tabType)
        recyclerViewCategory.adapter = categoryAdapter

        loadingIndicator = view.findViewById(R.id.loadingIndicator1)

        loadCategoriesFromFirestore(tabType)

        return view
    }


    fun loadCategoriesFromFirestore(type: String) {
        if (!isAdded) {
            Log.e("KategoriFragmentRec", "Fragment is not attached to activity. Skipping load.")
            return
        }

        Log.d("KategoriFragmentRec", "Loading categories for type: $type")
        loadingIndicator.visibility = View.VISIBLE

        firestore.collection("kategori")
            .whereEqualTo("userid", currentUid)
            .whereEqualTo("type", type)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                if (!isAdded) return@addOnSuccessListener

                Log.d("FirestoreSuccess", "Documents retrieved: ${documents.size()}")
                val categories = mutableListOf<CategoryItem>()
                if (documents.isEmpty) {
                    Log.d("FirestoreEmpty", "No categories found for type: $type")
                    showEmptyState()
                } else {
                    categories.clear()
                    for (document in documents) {
                        val categoryName = document.getString("name") ?: "Unknown"
                        val categoryIcon = document.getString("icon")
                        categories.add(CategoryItem(categoryName, getIconDrawable(categoryIcon)))
                    }
                    updateCategoryList(categories)
                }
                loadingIndicator.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                if (!isAdded) return@addOnFailureListener
                Log.e("FirestoreError", "Failed to load categories", e)
                showErrorState()
                loadingIndicator.visibility = View.GONE
            }
    }


    fun refreshCategories() {
        Log.d("KategoriFragmentRec", "Refreshing categories for tabType: $tabType")
        loadCategoriesFromFirestore(tabType)
    }

    private fun updateCategoryList(categories: List<CategoryItem>) {
        categoryAdapter.updateCategories(categories)
    }

    private fun showEmptyState() {
        context?.let {
            Toast.makeText(it, "No categories found", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showErrorState() {
        Toast.makeText(requireContext(), "Error loading categories", Toast.LENGTH_SHORT).show()
    }

    private fun getIconDrawable(iconName: String?): Int {
        return when (iconName) {
            "ic_rapat" -> R.drawable.ic_rapat
            "ic_present" -> R.drawable.ic_present
            "ic_proyektor" -> R.drawable.ic_proyektor
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
            else -> R.drawable.ic_bc_profil // Default icon
        }
    }
}
