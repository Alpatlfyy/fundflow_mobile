package com.example.fundflow.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.google.firebase.firestore.FirebaseFirestore

class NoteAddActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.noteadd_activity)

        firestore = FirebaseFirestore.getInstance()

        val backButton = findViewById<ImageButton>(R.id.btnkembali)
        backButton.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "note")
            setResult(RESULT_OK) // Notify that a note was added
            startActivity(intent)
            finish()
        }

        val saveButton = findViewById<ImageButton>(R.id.tambahCatatan)
        saveButton.setOnClickListener {
            onSaveNoteClick(it)
        }
    }

    fun onSaveNoteClick(view: View) {

        val noteTitle = findViewById<EditText>(R.id.note_title)
        val noteContent = findViewById<EditText>(R.id.note_content)

        val titleText = noteTitle.text.toString()
        val contentText = noteContent.text.toString()

        if (titleText.isBlank() || contentText.isBlank()) {
            Toast.makeText(this, "Judul atau isi catatan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else {
            saveNoteToDatabase(titleText, contentText)
        }
    }

    private fun saveNoteToDatabase(title: String, content: String) {
        val currentUid = UserSingleton.getUid()
        val note = hashMapOf(
            "title" to title,
            "content" to content,
            "userid" to currentUid
        )

        firestore.collection("catatan")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Catatan berhasil disimpan", Toast.LENGTH_SHORT).show()

                // Clear form after saving
                findViewById<EditText>(R.id.note_title).text.clear()
                findViewById<EditText>(R.id.note_content).text.clear()

                // Set result to notify that new data has been added
                val intent = Intent()
                setResult(RESULT_OK, intent) // Notifies that the note was added
                finish() // Finish activity and return to the previous screen
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menyimpan catatan: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
