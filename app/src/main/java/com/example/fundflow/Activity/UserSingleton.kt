package com.example.fundflow

object UserSingleton {
    private val user = User()

    // Fungsi untuk mendapatkan UID
    fun getUid(): String {
        return user.getUid()
    }

    // Fungsi untuk menyimpan UID
    fun setUid(uid: String) {
        user.setUid(uid)
    }
}
