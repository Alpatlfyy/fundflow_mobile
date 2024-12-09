package com.example.fundflow.Domain

import com.google.firebase.Timestamp

data class ExpenseDomain(
    val id: String = "", // ID dokumen Firestore
    val icon: String = "",
    val jenis: String = "",
    val jumlah: Double = 0.0,
    val kategori: String = "",
    val tanggal: com.google.firebase.Timestamp = com.google.firebase.Timestamp.now()
)

