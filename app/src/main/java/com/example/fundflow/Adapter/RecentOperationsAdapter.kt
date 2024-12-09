package com.example.fundflow.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.databinding.ViewholderItemsBinding
import com.example.fundflow.model.RecentOperation
import java.text.DecimalFormat
import com.google.firebase.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecentOperationsAdapter(
    private val operations: MutableList<RecentOperation>
) : RecyclerView.Adapter<RecentOperationsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val operation = operations[position]

        // Set ikon
        holder.binding.pic.setImageResource(operation.icon)

        // Set nama
        holder.binding.titleTxt.text = operation.name

        // Set jumlah dengan format
        operation.amount?.let { amount ->
            // Tentukan warna berdasarkan tipe operasi (pemasukan atau pengeluaran)
            val textColor = when (operation.type) {
                "pengeluaran" -> "#FF4747"  // Merah untuk pengeluaran
                "pemasukan" -> "#00BC78"    // Hijau untuk pemasukan
                else -> "#000000"            // Default warna hitam
            }
            holder.binding.priceTxt.setTextColorSafe(textColor)

            // Format jumlah uang
            val formattedAmount = "Rp ${DecimalFormat("#,###.00").format(amount)}"
            holder.binding.priceTxt.text = formattedAmount
        } ?: run {
            holder.binding.priceTxt.text = ""
        }

        // Set tanggal
        operation.date?.let { date ->
            try {
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                val parsedDate = dateFormat.parse(date)
                if (parsedDate != null) {
                    holder.binding.timeTxt.text = dateFormat.format(parsedDate)
                }
            } catch (e: ParseException) {
                holder.binding.timeTxt.text = "Tanggal tidak valid"
            }
        }
    }

    override fun getItemCount(): Int = operations.size

    fun updateData(newOperations: List<RecentOperation>) {
        Log.d("AdapterUpdate", newOperations.toString())
        operations.clear()
        operations.addAll(newOperations)
        notifyDataSetChanged()
    }

    // Format Date menjadi String
    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
        return dateFormat.format(date)
    }
}

// Extension function untuk setTextColorSafe
fun TextView.setTextColorSafe(colorString: String) {
    try {
        this.setTextColor(Color.parseColor(colorString))
    } catch (e: IllegalArgumentException) {
        this.setTextColor(Color.BLACK) // Set default color jika ada error
    }
}
