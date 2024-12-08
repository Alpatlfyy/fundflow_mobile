package com.example.fundflow.Activity

import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import com.example.fundflow.R
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resetpassword)

        // Inisialisasi FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Inisialisasi komponen
        val emailEditText = findViewById<EditText>(R.id.emailtext)
        val resetPasswordButton = findViewById<Button>(R.id.resetpassbutton)
        val backToLoginText = findViewById<TextView>(R.id.backToLoginText)

        // Tombol Reset Password
        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isBlank()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mengirimkan email reset password
            sendPasswordResetEmail(email)
        }

        // Kembali ke halaman login
        backToLoginText.setOnClickListener {
            finish() // Menutup aktivitas ini dan kembali ke login
        }
    }

    // Fungsi untuk mengirimkan email reset password
    private fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Link reset password telah dikirim ke email Anda.", Toast.LENGTH_LONG).show()
                    finish() // Kembali ke halaman login setelah berhasil
                } else {
                    Toast.makeText(this, "Terjadi kesalahan: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
