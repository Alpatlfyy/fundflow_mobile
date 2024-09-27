package com.example.fundflow.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.fundflow.R

class RegisterActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_register)

        // Inisialisasi Spinner dan Icon
        val userRoleSpinner = findViewById<Spinner>(R.id.userRoleSpinner)
        val dropdownIcon = findViewById<ImageView>(R.id.dropdownIcon)
        val MasukText = findViewById<TextView>(R.id.MasukText)

        // Buat adapter menggunakan layout custom spinner_item
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.user_roles,
            R.layout.spinner_item_with_icon
        )

        // Set layout untuk dropdown saat dibuka (pop-up)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Pasang adapter ke Spinner
        userRoleSpinner.adapter = adapter

        // Fungsi untuk membuka Spinner ketika icon diklik
        dropdownIcon.setOnClickListener {
            userRoleSpinner.performClick() // Membuka spinner
        }

        // Listener untuk menangani pemilihan item
        userRoleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedRole = parent.getItemAtPosition(position).toString()
                // Anda bisa melakukan sesuatu dengan selectedRole di sini
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Setup focus change untuk EditText
        setupEditTextFocusChange(R.id.nameLayout, R.id.nameEditText)
        setupEditTextFocusChange(R.id.usernameLayout2, R.id.usernameEditText3)
        setupEditTextFocusChange(R.id.usernameLayout3, R.id.usernameEditText4)
        setupEditTextFocusChange(R.id.usernameLayout6, R.id.usernameEditText6)


        MasukText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("INITIAL_PAGE", 2) // Mengirimkan halaman ketiga (index 2)
            startActivity(intent)
        }
    }


    private fun setupEditTextFocusChange(layoutId: Int, editTextId: Int) {
        val layout = findViewById<LinearLayout>(layoutId)
        val editText = findViewById<EditText>(editTextId)

        // Listener untuk mengubah background ketika layout di klik
        layout.setOnClickListener {
            editText.requestFocus()
            layout.setBackgroundResource(R.drawable.custom_edittext_focused)
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                layout.setBackgroundResource(R.drawable.custom_edittext_focused)
            } else {
                layout.setBackgroundResource(R.drawable.custom_edittext_selector)
            }
        }
    }
}