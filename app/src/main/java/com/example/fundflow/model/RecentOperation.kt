package com.example.fundflow.model

data class RecentOperation(
    val type: String,
    val name: String,
    val icon: Int,
    val amount: Double? = null,
    val date: String? = null,
    var timestamp: Long? = null // Timestamp untuk penyortiran
)

