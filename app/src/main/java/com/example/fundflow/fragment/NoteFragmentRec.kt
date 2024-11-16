package com.example.fundflow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.Activity.NoteAdapter
import com.example.fundflow.model.NoteItem
import com.example.fundflow.R
import com.google.firebase.firestore.FirebaseFirestore

class NoteFragmentRec : Fragment() {
    private lateinit var recyclerViewNote: RecyclerView
    private lateinit var categoryAdapter: NoteAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val noteList = mutableListOf<NoteItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_recycleview_fragment, container, false)

        // Initialize RecyclerView
        recyclerViewNote = view.findViewById(R.id.view3)
        recyclerViewNote.layoutManager = LinearLayoutManager(context)

        // Initialize Adapter
        categoryAdapter = NoteAdapter(requireContext(), noteList)
        recyclerViewNote.adapter = categoryAdapter

        // Load categories from Firestore
        loadCategoriesFromFirestore() // change to "Pengeluaran" for different fragment if needed

        return view
    }

    private fun loadCategoriesFromFirestore() {
        println("Loading all categories")  // Debug log

        firestore.collection("catatan")
            .get()
            .addOnSuccessListener { documents ->
                noteList.clear()
                if (documents.isEmpty) {
                    println("No documents found") // Jika tidak ada dokumen ditemukan
                    showEmptyState()
                } else {
                    for (document in documents) {
                        val title = document.getString("title") ?: "Unknown"
                        val content = document.getString("content") ?: ""
                        noteList.add(NoteItem(title, content))
                    }
                    categoryAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
                showErrorState()
            }
    }



    private fun showEmptyState() {
        println("No categories available.")
    }

    private fun showErrorState() {
        println("Failed to load categories.")
    }
}
