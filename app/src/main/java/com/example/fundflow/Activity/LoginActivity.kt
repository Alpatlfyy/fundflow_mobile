package com.example.fundflow.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        firebaseAuth = FirebaseAuth.getInstance()

        // Menampilkan BottomSheet saat Activity diluncurkan
        showLoginBottomSheet(this)
    }

    companion object {
        fun showLoginBottomSheet(context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_login, null)
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTransparant)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            // Inisialisasi komponen UI di bottom sheet
            val registerText = view.findViewById<TextView>(R.id.registerText)
            val loginButton = view.findViewById<Button>(R.id.loginButton)
            val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
            val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
            val passwordToggle = view.findViewById<ImageView>(R.id.passwordToggle)
            val forgotPasswordText = view.findViewById<TextView>(R.id.forgotPasswordText)  // Tombol Lupa Password

            // Menangani klik pada teks register
            registerText.setOnClickListener {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }

            // Menangani klik pada tombol login
            loginButton.setOnClickListener {
                val email = usernameEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Ambil UID pengguna yang login
                            val user = FirebaseAuth.getInstance().currentUser
                            val uid = user?.uid

                            if (uid != null) {
                                // Simpan UID ke UserSingleton
                                UserSingleton.setUid(uid)


                                // Lanjutkan ke DashboardActivity
                                context.startActivity(Intent(context, DashboardActivity::class.java))
                            } else {
                                Toast.makeText(context, "Gagal mendapatkan UID pengguna.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Login gagal
                            Toast.makeText(context, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }



            // Menangani klik pada teks lupa password
            forgotPasswordText.setOnClickListener {
                context.startActivity(Intent(context, ResetPassword::class.java))  // Arahkan ke ForgotPasswordActivity
            }

            // Toggle visibility untuk password
            var isPasswordVisible = false
            passwordToggle.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                passwordEditText.inputType = if (isPasswordVisible) {
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                passwordToggle.setImageResource(if (isPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_closed)
            }
        }
    }
}
