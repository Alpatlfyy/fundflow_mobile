package com.example.fundflow.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fundflow.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    companion object {
        fun showLoginBottomSheet(context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_login, null)
            val bottomSheetDialog = BottomSheetDialog(context)
            bottomSheetDialog.setContentView(view)

            // Inisialisasi komponen UI di bottom sheet
            val registerText = view.findViewById<TextView>(R.id.registerText)
            val loginButton = view.findViewById<Button>(R.id.loginButton)
            val usernameLayout = view.findViewById<LinearLayout>(R.id.usernameLayout)
            val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
            val passwordLayout = view.findViewById<LinearLayout>(R.id.passwordLayout)
            val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
            val passwordToggle = view.findViewById<ImageView>(R.id.passwordToggle)

            registerText.setOnClickListener {
                context.startActivity(Intent(context, RegisterActivity::class.java))
            }

            loginButton.setOnClickListener {
                Log.d("LoginActivity", "Login button clicked")
                context.startActivity(Intent(context, DashboardActivity::class.java))
            }

            // Listener untuk fokus dan klik di username
            usernameEditText.setOnClickListener {
                usernameEditText.requestFocus()
                usernameLayout.setBackgroundResource(R.drawable.custom_edittext_focused)
            }

            // Mengubah background saat fokus pada username
            usernameEditText.setOnFocusChangeListener { _, hasFocus ->
                usernameLayout.setBackgroundResource(
                    if (hasFocus) R.drawable.custom_edittext_focused else R.drawable.custom_edittext_selector
                )
            }

            usernameLayout.setOnClickListener {
                usernameEditText.requestFocus()
            }

            // Listener untuk fokus dan klik di password
            passwordEditText.setOnClickListener {
                passwordEditText.requestFocus()
                passwordLayout.setBackgroundResource(R.drawable.custom_edittext_focused)
            }

            passwordEditText.setOnFocusChangeListener { _, hasFocus ->
                passwordLayout.setBackgroundResource(
                    if (hasFocus) R.drawable.custom_edittext_focused else R.drawable.custom_edittext_selector
                )
            }

            passwordLayout.setOnClickListener {
                passwordEditText.requestFocus()
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

            bottomSheetDialog.show()
        }
    }
}
