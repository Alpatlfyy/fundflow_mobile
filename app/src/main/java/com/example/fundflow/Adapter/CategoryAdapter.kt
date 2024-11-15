package com.example.fundflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.Activity.CategoryactionActivity
import com.example.fundflow.R
import com.example.fundflow.model.CategoryItem

class CategoryAdapter(
    private val context: Context, // Tambahkan context untuk memanggil BottomSheet
    private val categoryList: MutableList<CategoryItem>,
    private val tabType: String
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    fun updateCategories(newCategoryList: List<CategoryItem>) {
        categoryList.clear()
        categoryList.addAll(newCategoryList)
        notifyDataSetChanged()  // Notifikasi perubahan data pada adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewcategory_items, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryItem = categoryList[position]
        holder.bind(categoryItem)

        // Tambahkan klik listener untuk ikon tiga titik
        holder.moreOptionsIcon.setOnClickListener {
            // Memanggil BottomSheet
            CategoryactionActivity.showBottomSheet(context, categoryItem.name, categoryItem.icon, tabType)
        }
    }

    override fun getItemCount(): Int = categoryList.size

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIcon: ImageView = itemView.findViewById(R.id.iconCategoryImageView)
        private val categoryName: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val moreOptionsIcon: ImageView = itemView.findViewById(R.id.titik3) // Ikon tiga titik

        fun bind(categoryItem: CategoryItem) {
            categoryIcon.setImageResource(categoryItem.icon)
            categoryName.text = categoryItem.name
        }
    }
}
