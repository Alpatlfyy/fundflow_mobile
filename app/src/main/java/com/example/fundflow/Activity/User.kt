package com.example.fundflow

class User(private var uid: String = "") {

    // Getter untuk UID
    fun getUid(): String {
        return uid
    }

    // Setter untuk UID
    fun setUid(newUid: String) {
        uid = newUid
    }
}
