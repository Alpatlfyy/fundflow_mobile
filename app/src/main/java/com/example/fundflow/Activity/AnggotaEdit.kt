package com.example.fundflow.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AnggotaEdit : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anggota_edit)

        firestore = FirebaseFirestore.getInstance()

        // Tombol Back
        val backButton = findViewById<ImageButton>(R.id.btnBack)
        backButton.setOnClickListener {
            val intent = Intent(this, AnggotaActivity::class.java)
            intent.putExtra("NAVIGATION_TARGET", "anggota")
            startActivity(intent)
            finish()
        }

        // Inisialisasi komponen UI
        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etNoTelepon = findViewById<EditText>(R.id.etNoTelepon)
        val etTanggal = findViewById<EditText>(R.id.etTanggal)
        val btnTanggal = findViewById<ImageView>(R.id.btnTanggal)
        val spinnerJabatan = findViewById<Spinner>(R.id.spinnerJenisJabatan)
        val spinnerJenisKelamin = findViewById<Spinner>(R.id.spinnerJenisKelamin)
        val btnSimpan = findViewById<Button>(R.id.tambahAnggota)

        // Data spinner
        val jabatanList = listOf("Ketua", "Sekretaris", "Bendahara", "Anggota")
        val jenisKelaminList = listOf("Laki-laki", "Perempuan")

        // Mengatur adapter untuk spinner
        val jabatanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jabatanList)
        val jenisKelaminAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminList)
        jabatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisKelaminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerJabatan.adapter = jabatanAdapter
        spinnerJenisKelamin.adapter = jenisKelaminAdapter

        // Mendapatkan data anggota dari intent
        val idAnggota = intent.getStringExtra("ID_ANGGOTA")
        val namaAnggota = intent.getStringExtra("NAMA_ANGGOTA")
        val emailAnggota = intent.getStringExtra("EMAIL_ANGGOTA")
        val noTeleponAnggota = intent.getStringExtra("NO_TELEPON_ANGGOTA")
        val tanggalAnggota = intent.getStringExtra("TANGGAL_ANGGOTA")
        val jabatanAnggota = intent.getStringExtra("JABATAN_ANGGOTA")
        val jenisKelaminAnggota = intent.getStringExtra("JENIS_KELAMIN_ANGGOTA")

        // Menampilkan data di komponen UI
        etNama.setText(namaAnggota ?: "")
        etEmail.setText(emailAnggota ?: "")
        etNoTelepon.setText(noTeleponAnggota ?: "")
        etTanggal.setText(tanggalAnggota ?: "")
        jabatanAnggota?.let { spinnerJabatan.setSelection(jabatanList.indexOf(it)) }
        jenisKelaminAnggota?.let { spinnerJenisKelamin.setSelection(jenisKelaminList.indexOf(it)) }

        // Date Picker untuk tanggal
        btnTanggal.setOnClickListener { showDatePicker(etTanggal) }
        etTanggal.setOnClickListener { showDatePicker(etTanggal) }

        // Menangani klik tombol Simpan
        btnSimpan.setOnClickListener {
            val updatedNama = etNama.text.toString().trim()
            val updatedEmail = etEmail.text.toString().trim()
            val updatedNoTelepon = etNoTelepon.text.toString().trim()
            val updatedTanggal = etTanggal.text.toString().trim()
            val updatedJabatan = spinnerJabatan.selectedItem.toString()
            val updatedJenisKelamin = spinnerJenisKelamin.selectedItem.toString()

            // Validasi data
            if (updatedNama.isEmpty() || updatedEmail.isEmpty() || updatedNoTelepon.isEmpty() || updatedTanggal.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan perubahan ke Firestore
            if (idAnggota != null) {
                val anggotaRef = firestore.collection("anggota").document(idAnggota)
                anggotaRef.update(
                    mapOf(
                        "nama" to updatedNama,
                        "email" to updatedEmail,
                        "noTelepon" to updatedNoTelepon,
                        "tanggalLahir" to updatedTanggal,
                        "jabatan" to updatedJabatan,
                        "jenisKelamin" to updatedJenisKelamin
                    )
                ).addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal memperbarui data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "ID anggota tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val themedContext = ContextThemeWrapper(this, R.style.BlueDatePicker)

        DatePickerDialog(
            themedContext,
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                editText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
