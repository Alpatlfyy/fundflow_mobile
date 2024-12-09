package com.example.fundflow.model

data class CategoryItem(
    val name: String = "",  // Pastikan memberikan nilai default
    val icon: Int = 0,      // Nilai default untuk icon
    val type: String = ""   // Nilai default untuk type
) {
    // Konstruktor default sudah ada karena kita menggunakan data class
}
