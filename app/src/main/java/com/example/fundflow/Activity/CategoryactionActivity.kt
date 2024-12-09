package com.example.fundflow.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.fundflow.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore

class CategoryactionActivity : Activity() {

    companion object {
        private val db = FirebaseFirestore.getInstance()

        fun showBottomSheet(
            context: Context,
            name: String?,
            icon: Int?,
            tabType: String?,
            onDeleteConfirmed: (String) -> Unit // Lambda untuk penghapusan kategori
        ) {
            val view = LayoutInflater.from(context).inflate(R.layout.activity_action_category, null)
            val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTransparant)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val ubahNamaLayout = view.findViewById<LinearLayout>(R.id.ubahTitle)
            val hapusDaftarLayout = view.findViewById<LinearLayout>(R.id.hapusTitle)

            ubahNamaLayout.setOnClickListener {
                val intent = Intent(context, EditCategoriActivity::class.java)
                intent.putExtra("CATEGORY_NAME", name)
                intent.putExtra("CATEGORY_ICON", icon)
                intent.putExtra("CATEGORY_TYPE", tabType ?: "Pemasukan")
                context.startActivity(intent)
                (context as Activity).finish()
            }

            hapusDaftarLayout.setOnClickListener {
                if (!name.isNullOrEmpty()) {
                    showDeleteConfirmationDialog(context, bottomSheetDialog, name) {
                        onDeleteConfirmed(name) // Menjalankan lambda untuk menghapus kategori
                        bottomSheetDialog.dismiss() // Menutup bottom sheet setelah penghapusan
                    }
                } else {
                    Toast.makeText(context, "Nama kategori tidak ditemukan!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun showDeleteConfirmationDialog(
            context: Context,
            bottomSheetDialog: BottomSheetDialog,
            name: String,
            onDeleteConfirmed: () -> Unit
        ) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null)
            val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
                .setView(dialogView)

            val dialogMessage: TextView = dialogView.findViewById(R.id.dialogMessage)
            val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
            val deleteButton: Button = dialogView.findViewById(R.id.deleteButton)

            dialogMessage.text = "Apakah kamu yakin ingin menghapus $name dari Daftar Tersimpan?"

            val alertDialog = dialogBuilder.create()

            cancelButton.setOnClickListener {
                alertDialog.dismiss()
            }

            deleteButton.setOnClickListener {
                // Menambahkan penghapusan kategori dari Firestore
                db.collection("kategori")
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(context, "Kategori tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        } else {
                            for (document in documents) {
                                db.collection("kategori").document(document.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "$name telah dihapus", Toast.LENGTH_SHORT).show()

                                        // Refresh fragment kategori setelah penghapusan
                                        // Jika Anda berada di dalam CategoryActivity, panggil fungsi di Activity untuk refresh fragment
                                        if (context is CategoryActivity) {
                                            context.refreshCategoryFragment()  // Memanggil fungsi di CategoryActivity
                                        }

                                        onDeleteConfirmed() // Memanggil lambda untuk memperbarui RecyclerView di activity/fragment
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, "Gagal menghapus $name", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Gagal menemukan kategori $name", Toast.LENGTH_SHORT).show()
                    }
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }
}
