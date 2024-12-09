package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.example.fundflow.model.AnggotaItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AnggotaActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var anggotaAdapter: AnggotaAdapter
    private val anggotaList = mutableListOf<AnggotaItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anggota_activity)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

        // Tombol Back
        val backButton = findViewById<ImageButton>(R.id.btnnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "dashboard")
            startActivity(intent)
            finish()
        }

        // Setup RecyclerView
        recyclerView = findViewById(R.id.allanggota)
        recyclerView.layoutManager = LinearLayoutManager(this)
        anggotaAdapter = AnggotaAdapter(anggotaList, ::onItemClicked, ::onDeleteClicked)
        recyclerView.adapter = anggotaAdapter

        // Floating Action Button untuk menambah anggota
        val btnTambah: FloatingActionButton = findViewById(R.id.tambahAnggota1)
        btnTambah.setOnClickListener {
            val intent = Intent(this, AnggotaAddActivity::class.java)
            startActivity(intent)
        }
        val currentUid = UserSingleton.getUid()

        // Dengarkan perubahan data di Firestore
        listenToAnggotaChanges()
    }

    // Fungsi untuk mendengarkan perubahan data anggota di Firestore
    private fun listenToAnggotaChanges() {
        val currentUid = UserSingleton.getUid()
        firestore.collection("anggota")
            .whereEqualTo("userid",currentUid)
            .orderBy("createdAt", Query.Direction.DESCENDING) // Urutkan berdasarkan waktu kirim
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Gagal memuat data: ${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                snapshots?.let { snapshot ->
                    // Bersihkan daftar anggota terlebih dahulu untuk memperbarui data terbaru
                    anggotaList.clear()
                    for (doc in snapshot) {
                        val anggota = AnggotaItem.fromMap(doc.data, doc.id)
                        anggotaList.add(anggota) // Tambahkan data tanpa mengatur urutan, Firestore sudah mengurutkan
                    }
                    anggotaAdapter.notifyDataSetChanged() // Memberi tahu adapter ada perubahan data
                }
            }
    }

    // Fungsi ini dipanggil ketika item dalam RecyclerView diklik
    private fun onItemClicked(anggota: AnggotaItem) {
        if (anggota.id.isNotEmpty()) {
            val intent = Intent(this, AnggotaEdit::class.java).apply {
                putExtra("ID_ANGGOTA", anggota.id)
                putExtra("NAMA_ANGGOTA", anggota.nama)
                putExtra("EMAIL_ANGGOTA", anggota.alamat)
                putExtra("NO_TELEPON_ANGGOTA", anggota.noTelepon)
                putExtra("TANGGAL_ANGGOTA", anggota.tanggalLahir)
                putExtra("JABATAN_ANGGOTA", anggota.jabatan)
                putExtra("JENIS_KELAMIN_ANGGOTA", anggota.jenisKelamin)
            }
            Log.d("Firestore", "Data yang dikirim ke Edit: $anggota")
            startActivity(intent)
        } else {
            Toast.makeText(this, "Data anggota tidak valid", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi ini dipanggil ketika tombol Hapus diklik pada AnggotaAdapter
    private fun onDeleteClicked(anggota: AnggotaItem) {
        if (anggota.id.isEmpty()) {
            Toast.makeText(this, "Data anggota tidak valid untuk dihapus", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus anggota ${anggota.nama}?")

        builder.setPositiveButton("Ya") { dialog, _ ->
            firestore.collection("anggota").document(anggota.id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Anggota ${anggota.nama} berhasil dihapus", Toast.LENGTH_SHORT).show()
                    anggotaList.remove(anggota) // Hapus dari daftar lokal
                    anggotaAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menghapus anggota: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            dialog.dismiss()
        }

        builder.setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
