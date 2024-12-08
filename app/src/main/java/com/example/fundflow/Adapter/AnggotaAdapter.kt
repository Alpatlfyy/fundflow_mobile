package com.example.fundflow.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.R
import com.example.fundflow.model.AnggotaItem

class AnggotaAdapter(
    private val anggotaList: List<AnggotaItem>, // List data anggota
    private val onItemClicked: (AnggotaItem) -> Unit, // Listener untuk klik item (untuk edit anggota)
    private val onDeleteClicked: (AnggotaItem) -> Unit // Listener untuk klik tombol hapus
) : RecyclerView.Adapter<AnggotaAdapter.AnggotaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggotaViewHolder {
        // Inflate layout item_anggota untuk setiap item dalam RecyclerView
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.anggota_item, parent, false)
        return AnggotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnggotaViewHolder, position: Int) {
        // Mengikat data anggota ke dalam view holder
        val anggota = anggotaList[position]
        holder.bind(anggota)
    }

    override fun getItemCount(): Int = anggotaList.size // Mengembalikan jumlah anggota dalam list

    // ViewHolder untuk setiap item anggota
    inner class AnggotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaTextView: TextView = itemView.findViewById(R.id.nama) // TextView untuk nama anggota
        private val jabatanTextView: TextView = itemView.findViewById(R.id.jabatan) // TextView untuk jabatan anggota
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelet) // Tombol hapus anggota

        fun bind(anggota: AnggotaItem) {
            // Menampilkan nama dan jabatan anggota
            namaTextView.text = anggota.nama
            jabatanTextView.text = anggota.jabatan

            // Mengatur listener untuk klik item (untuk membuka detail anggota atau halaman edit)
            itemView.setOnClickListener {
                onItemClicked(anggota) // Mengirim anggota yang diklik ke fungsi onItemClicked
            }

            // Mengatur listener untuk tombol hapus
            btnDelete.setOnClickListener {
                onDeleteClicked(anggota) // Mengirim anggota yang dipilih ke fungsi onDeleteClicked
            }
        }
    }
}
