package com.example.fundflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.Activity.CategoryactionActivity.Companion.showBottomSheet
import com.example.fundflow.R
import com.example.fundflow.model.CategoryItem
import com.google.firebase.firestore.FirebaseFirestore

class CategoryAdapter(
    private val context: Context,
    private val categoryList: MutableList<CategoryItem>,
    private val tabType: String
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val firestore = FirebaseFirestore.getInstance()

    // Fungsi untuk memperbarui daftar kategori
    fun updateCategories(newCategoryList: List<CategoryItem>) {
        categoryList.clear()
        categoryList.addAll(newCategoryList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewcategory_items, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        holder.bind(categoryItem)

        // Menangani klik untuk opsi lainnya (titik 3)
        holder.moreOptionsIcon.setOnClickListener {
            // Menampilkan BottomSheet untuk memilih tindakan
            showBottomSheet(
                context,
                categoryItem.name,
                categoryItem.icon,
                tabType // pastikan tabType ini sudah benar nilainya (misalnya "Pemasukan" atau "Pengeluaran")
            ) { categoryName ->
                // Menghapus kategori dan memanggil update data
                deleteCategory(categoryName, position)
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size

    private fun deleteCategory(categoryName: String, position: Int) {
        firestore.collection("kategori")
            .whereEqualTo("name", categoryName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    // Hanya menghapus kategori jika ditemukan di Firestore
                    val documentSnapshot = querySnapshot.documents[0]
                    documentSnapshot.reference.delete()
                        .addOnSuccessListener {
                            // Menghapus kategori dari daftar lokal dan memberi tahu adapter untuk memperbarui RecyclerView
                            categoryList.removeAt(position)
                            notifyItemRemoved(position)
                            Toast.makeText(context, "$categoryName berhasil dihapus", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error deleting category", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Kategori tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error retrieving category", Toast.LENGTH_SHORT).show()
            }
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIcon: ImageView = itemView.findViewById(R.id.iconCategoryImageView)
        private val categoryName: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val moreOptionsIcon: ImageView = itemView.findViewById(R.id.titik3)

        fun bind(categoryItem: CategoryItem) {
            categoryIcon.setImageResource(categoryItem.icon)
            categoryName.text = categoryItem.name
        }
    }
}
