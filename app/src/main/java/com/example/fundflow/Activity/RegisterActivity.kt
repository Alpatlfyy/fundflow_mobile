package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.fundflow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firestore dan FirebaseAuth
        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        // Inisialisasi komponen
        val userRoleSpinner = findViewById<Spinner>(R.id.userRoleSpinner)
        val dropdownIcon = findViewById<ImageView>(R.id.dropdownIcon)
        val masukText = findViewById<TextView>(R.id.MasukText)
        val registerButton = findViewById<Button>(R.id.loginButton)

        // Atur adapter untuk Spinner
        setupSpinner(userRoleSpinner, dropdownIcon)

        // Navigasi ke halaman login saat teks "Masuk" diklik
        masukText.setOnClickListener {
            navigateToMainActivity(2)
        }

        // Tombol daftar
        registerButton.setOnClickListener {
            performRegistration()
        }
    }

    // Fungsi untuk mengatur Spinner dan ikonnya
    private fun setupSpinner(spinner: Spinner, icon: ImageView) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_roles,
            R.layout.spinner_item_with_icon
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        icon.setOnClickListener {
            spinner.performClick()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedRole = parent.getItemAtPosition(position).toString()
                Toast.makeText(this@RegisterActivity, "Role: $selectedRole", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Kosongkan jika tidak ada yang dipilih
            }
        }
    }

    // Navigasi ke MainActivity
    private fun navigateToMainActivity(initialPage: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("INITIAL_PAGE", initialPage)
        startActivity(intent)
    }

    // Fungsi untuk memproses registrasi
    private fun performRegistration() {
        // Ambil input dari EditText
        val name = findViewById<EditText>(R.id.nameEditText).text.toString().trim()
        val username = findViewById<EditText>(R.id.usernameEditText3).text.toString().trim()
        val email = findViewById<EditText>(R.id.usernameEditText4).text.toString().trim()
        val password = findViewById<EditText>(R.id.usernameEditText6).text.toString().trim()
        val userRole = findViewById<Spinner>(R.id.userRoleSpinner).selectedItem.toString()

        // Validasi input
        if (name.isBlank() || username.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Semua bidang harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Cek format email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show()
            return
        }

        // Proses pendaftaran dengan Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Jika berhasil, simpan data ke Firestore
                    val userData = hashMapOf(
                        "name" to name,
                        "username" to username,
                        "email" to email,
                        "role" to userRole
                    )

                    firestore.collection("register")
                        .document(task.result?.user?.uid ?: "")
                        .set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity(2) // Navigasi ke halaman login
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Jika gagal, tampilkan pesan kesalahan
                    Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
