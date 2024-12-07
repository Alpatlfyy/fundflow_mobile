package com.example.fundflow.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.Activity.NoteAdapter
import com.example.fundflow.Activity.NoteEdit
import com.example.fundflow.R
import com.example.fundflow.model.NoteItem
import com.google.firebase.firestore.FirebaseFirestore

class NoteFragmentRec : Fragment() {
    private lateinit var recyclerViewNote: RecyclerView
    private lateinit var categoryAdapter: NoteAdapter
    private val noteList = mutableListOf<NoteItem>()
    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        const val EDIT_NOTE_REQUEST_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_fragment_recycleview, container, false)

        recyclerViewNote = view.findViewById(R.id.view3)
        recyclerViewNote.layoutManager = LinearLayoutManager(context)

        categoryAdapter = NoteAdapter(requireContext(), noteList, { note ->
            showDeleteConfirmationDialog(note)
        }, { note ->
            val intent = Intent(requireContext(), NoteEdit::class.java)
            intent.putExtra("noteId", note.id)
            intent.putExtra("noteTitle", note.title)
            intent.putExtra("noteContent", note.content)
            startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
        })

        recyclerViewNote.adapter = categoryAdapter
        loadCategoriesFromFirestore()

        return view
    }

    private fun loadCategoriesFromFirestore() {
        firestore.collection("catatan")
            .get()
            .addOnSuccessListener { documents ->
                noteList.clear()
                for (document in documents) {
                    val title = document.getString("title") ?: "Unknown"
                    val content = document.getString("content") ?: ""
                    val id = document.id
                    val note = NoteItem(id, title, content)
                    noteList.add(0, note)
                }
                categoryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }

    private fun showDeleteConfirmationDialog(note: NoteItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Penghapusan")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
        builder.setPositiveButton("Ya") { dialog, _ ->
            deleteNoteFromFirestore(note)
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deleteNoteFromFirestore(note: NoteItem) {
        firestore.collection("catatan").document(note.id).delete()
            .addOnSuccessListener {
                noteList.remove(note)
                categoryAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                println("Error deleting note: ${it.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val noteId = data?.getStringExtra("noteId")
            val updatedTitle = data?.getStringExtra("noteTitle")
            val updatedContent = data?.getStringExtra("noteContent")

            if (noteId != null && updatedTitle != null && updatedContent != null) {
                val index = noteList.indexOfFirst { it.id == noteId }
                if (index != -1) {
                    noteList[index].title = updatedTitle
                    noteList[index].content = updatedContent
                    categoryAdapter.notifyItemChanged(index)
                } else {
                    noteList.add(0, NoteItem(noteId, updatedTitle, updatedContent))
                    categoryAdapter.notifyItemInserted(0)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCategoriesFromFirestore()
    }
}
