package com.example.fundflow.Activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.R
import com.example.fundflow.model.NoteItem
import com.google.firebase.firestore.FirebaseFirestore

class NoteAdapter(
    private val context: Context,
    private var notes: MutableList<NoteItem>,
    private val onDeleteClick: (NoteItem) -> Unit,
    private val onEditClick: (NoteItem) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        private val contentTextView: TextView = itemView.findViewById(R.id.note_description)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete)

        fun bind(note: NoteItem) {
            titleTextView.text = note.title
            contentTextView.text = note.content

            // Klik pada item untuk membuka menu edit
            itemView.setOnClickListener { onEditClick(note) }

            // Klik tombol hapus untuk menghapus catatan
            deleteButton.setOnClickListener { onDeleteClick(note) }
        }
    }

    fun updateItem(note: NoteItem) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
            notifyItemChanged(index)
        }
    }

    fun removeItem(note: NoteItem) {
        val position = notes.indexOf(note)
        if (position != -1) {
            notes.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
