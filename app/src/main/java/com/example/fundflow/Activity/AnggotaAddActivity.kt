package com.example.fundflow.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.fundflow.R
import com.example.fundflow.model.AnggotaItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.util.Calendar

class AnggotaAddActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anggota_add_activity)

        // Inisialisasi Firestore
        firestore = FirebaseFirestore.getInstance()

        // Tombol Kembali
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            navigateBackToAnggotaActivity()
        }

        // Inisialisasi komponen UI
        val etNamaAnggota: EditText = findViewById(R.id.etNama)
        val etAlamatAnggota: EditText = findViewById(R.id.etEmail)
        val etTanggal: EditText = findViewById(R.id.etTanggal)
        val etNoTelepon: EditText = findViewById(R.id.etNoTelepon)
        val btnTanggal: ImageView = findViewById(R.id.btnTanggal)
        val btnSimpanAnggota: Button = findViewById(R.id.tambahAnggota)

        // Spinner Jabatan dan Jenis Kelamin
        val spinnerJabatan: Spinner = findViewById(R.id.spinnerJenisJabatan)
        val spinnerJenisKelamin: Spinner = findViewById(R.id.spinnerJenisKelamin)

        // Data spinner
        setupSpinners(spinnerJabatan, spinnerJenisKelamin)

        // Tanggal Picker
        setupDatePicker(etTanggal, btnTanggal)

        // Aksi simpan anggota
        btnSimpanAnggota.setOnClickListener {
            val anggotaData = collectAnggotaData(
                etNamaAnggota, etAlamatAnggota, etTanggal, etNoTelepon,
                spinnerJabatan, spinnerJenisKelamin
            )

            if (anggotaData != null) {
                saveAnggotaDataToFirestore(anggotaData)
            } else {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateBackToAnggotaActivity() {
        val intent = Intent(this, AnggotaActivity::class.java)
        intent.putExtra("NAVIGATION_TARGET", "anggota")
        startActivity(intent)
        finish()
    }

    private fun setupSpinners(spinnerJabatan: Spinner, spinnerJenisKelamin: Spinner) {
        val jabatanList = listOf("Ketua", "Sekretaris", "Bendahara", "Anggota")
        val jenisKelaminList = listOf("Laki-laki", "Perempuan")

        val jabatanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jabatanList)
        val jenisKelaminAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKelaminList)

        jabatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJabatan.adapter = jabatanAdapter
        spinnerJabatan.setSelection(0) // Set default value

        jenisKelaminAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKelamin.adapter = jenisKelaminAdapter
        spinnerJenisKelamin.setSelection(0) // Set default value
    }

    private fun setupDatePicker(etTanggal: EditText, btnTanggal: ImageView) {
        val calendar = Calendar.getInstance()
        val themedContext = ContextThemeWrapper(this, R.style.BlueDatePicker)

        // Set onClickListener for date picker button
        btnTanggal.setOnClickListener { showDatePicker(etTanggal) }

        // Set onClickListener for EditText to show date picker
        etTanggal.setOnClickListener { showDatePicker(etTanggal) }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val themedContext = ContextThemeWrapper(this, R.style.BlueDatePicker)

        DatePickerDialog(
            themedContext,
            { _, year, month, day ->
                editText.setText("$day/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun collectAnggotaData(
        etNamaAnggota: EditText, etAlamatAnggota: EditText, etTanggal: EditText,
        etNoTelepon: EditText, spinnerJabatan: Spinner, spinnerJenisKelamin: Spinner
    ): HashMap<String, Any>? {
        val nama = etNamaAnggota.text.toString().trim()
        val alamat = etAlamatAnggota.text.toString().trim()
        val tanggalLahir = etTanggal.text.toString().trim()
        val noTelepon = etNoTelepon.text.toString().trim()
        val jabatan = spinnerJabatan.selectedItem.toString()
        val jenisKelamin = spinnerJenisKelamin.selectedItem.toString()

        // Validasi input
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(alamat) || TextUtils.isEmpty(tanggalLahir) || TextUtils.isEmpty(noTelepon)) {
            return null
        }

        // Mengumpulkan data dalam bentuk HashMap
        return hashMapOf(
            "nama" to nama,
            "alamat" to alamat,
            "tanggalLahir" to tanggalLahir,
            "noTelepon" to noTelepon,
            "jabatan" to jabatan,
            "jenisKelamin" to jenisKelamin,
            "createdAt" to FieldValue.serverTimestamp() // Tambahkan timestamp server
        )
    }

    private fun saveAnggotaDataToFirestore(anggotaData: HashMap<String, Any>) {
        firestore.collection("anggota")
            .add(anggotaData)
            .addOnSuccessListener {
                Toast.makeText(this, "Data Anggota berhasil disimpan", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, Intent())
                finish() // Tutup aktivitas
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
