package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.google.firebase.firestore.FirebaseFirestore

class NoteEdit : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var noteTitleEditText: EditText
    private lateinit var noteContentEditText: EditText
    private lateinit var saveButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_edit)

        // Tombol Back
        val backButton = findViewById<ImageButton>(R.id.btnkembali)
        backButton.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "catatan")
            startActivity(intent)
            finish()
        }

        // Initialize Views
        noteTitleEditText = findViewById(R.id.note_title)
        noteContentEditText = findViewById(R.id.note_content)
        saveButton = findViewById(R.id.tambahCatatan)

        // Get the note data from intent
        val noteId = intent.getStringExtra("noteId")
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")

        // Set initial values
        noteTitleEditText.setText(noteTitle)
        noteContentEditText.setText(noteContent)

        saveButton.setOnClickListener {
            val updatedTitle = noteTitleEditText.text.toString()
            val updatedContent = noteContentEditText.text.toString()

            // Save the updated note to Firestore
            if (noteId != null) {
                firestore.collection("catatan").document(noteId)
                    .update("title", updatedTitle, "content", updatedContent)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Catatan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after update
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal memperbarui catatan", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
