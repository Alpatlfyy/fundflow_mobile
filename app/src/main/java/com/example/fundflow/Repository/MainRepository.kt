package com.example.fundflow.Repository

import com.example.fundflow.Domain.ExpenseDomain
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MainRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val collection = firestore.collection("aruskas")

    suspend fun fetchExpenses(): List<ExpenseDomain> {
        return try {
            val snapshot = collection.get().await() // Ambil data Firestore
            snapshot.documents.mapNotNull { document ->
                document.toObject(ExpenseDomain::class.java) // Mapping data ke ExpenseDomain
            }
        } catch (e: Exception) {
            emptyList() // Jika ada kesalahan, kembalikan daftar kosong
        }
    }
}
