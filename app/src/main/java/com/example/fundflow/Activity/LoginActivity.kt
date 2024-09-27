package com.example.fundflow.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fundflow.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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
            val usernameLayout = view.findViewById<LinearLayout>(R.id.usernameLayout)
            val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
            val passwordLayout = view.findViewById<LinearLayout>(R.id.passwordLayout)
            val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
            val passwordToggle = view.findViewById<ImageView>(R.id.passwordToggle)

            // Menangani klik pada teks register
            registerText.setOnClickListener {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }

            // Menangani klik pada tombol login
            loginButton.setOnClickListener {
                Log.d("LoginActivity", "Login button clicked")
                context.startActivity(Intent(context, DashboardActivity::class.java))
            }

            // Listener untuk fokus dan klik di username
            usernameLayout.setOnClickListener {
                usernameEditText.requestFocus()
            }

            // Mengubah background saat fokus pada username
            usernameEditText.setOnFocusChangeListener { _, hasFocus ->
                usernameLayout.setBackgroundResource(
                    if (hasFocus) R.drawable.custom_edittext_focused else R.drawable.custom_edittext_selector
                )
            }

            // Listener untuk fokus dan klik di password
            passwordLayout.setOnClickListener {
                passwordEditText.requestFocus()
            }

            passwordEditText.setOnFocusChangeListener { _, hasFocus ->
                passwordLayout.setBackgroundResource(
                    if (hasFocus) R.drawable.custom_edittext_focused else R.drawable.custom_edittext_selector
                )
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
                passwordEditText.typeface = ResourcesCompat.getFont(context, R.font.poppins_light)
                passwordEditText.setSelection(passwordEditText.text.length)
                passwordEditText.requestFocus()
            }

            // Menambahkan listener untuk menyembunyikan keyboard saat mengklik di luar EditText
            view.setOnClickListener {
                hideKeyboard(view)
                usernameEditText.clearFocus()
                passwordEditText.clearFocus()
            }

            // Menampilkan BottomSheetDialog
            bottomSheetDialog.show()
            bottomSheetDialog.setOnShowListener {
                val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.let {
                    // Mengatur layout
                    it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    it.background = ContextCompat.getDrawable(context, R.drawable.rounded_background)

                    // Mengatur Gravity
                    val window = bottomSheetDialog.window
                    window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.rounded_background))
                    window?.setGravity(Gravity.BOTTOM)
                }
            }

        }

        // Fungsi untuk menyembunyikan keyboard
        private fun hideKeyboard(view: View) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
