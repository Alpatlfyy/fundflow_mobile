package com.example.fundflow.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R
import com.google.firebase.firestore.FirebaseFirestore

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import java.util.Calendar


class AddInvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundflowTheme {
                mainStateAddInvoiceActivity(this)
            }
        }
    }
}

@Composable
fun mainStateAddInvoiceActivity(activity: AppCompatActivity? = null) {
    val context = LocalContext.current

    val companyNameState = remember { mutableStateOf("") }
    val companyAddressState = remember { mutableStateOf("") }
    val invoiceNumberState = remember { mutableStateOf("") }
    val invoiceDateState = remember { mutableStateOf("") }
    val customerNameState = remember { mutableStateOf("") }
    val customerAddressState = remember { mutableStateOf("") }
    val taxAmountState = remember { mutableStateOf("") }
    val itemsState = remember { mutableStateOf(listOf<String>()) }
    val selectedStatusState = remember { mutableStateOf("Lunas") }
    var logoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Inisialisasi launcher untuk mendapatkan gambar dari galeri
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val selectedMediaUri = data.data
            selectedMediaUri?.let { uri ->
                // Konversi URI ke Bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                logoBitmap = bitmap // Simpan bitmap di logoBitmap
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back Arrow",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Tambah Invoice",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Logo Perusahaan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = if (logoBitmap != null) {
                            BitmapPainter(logoBitmap!!.asImageBitmap())
                        } else {
                            painterResource(id = R.drawable.ic_media_upload)
                        },
                        contentDescription = "Upload Media",
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Start)
                            .padding(start = 16.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                launcher.launch(intent)
                            },
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                items(listOf("Nama Perusahaan", "Alamat Perusahaan", "Nomor Invoice", "Nama Pelanggan", "Alamat Pelanggan", "Pajak")) { label ->
                    val textState = when (label) {
                        "Nama Perusahaan" -> companyNameState
                        "Alamat Perusahaan" -> companyAddressState
                        "Nomor Invoice" -> invoiceNumberState
                        "Tanggal Invoice" -> invoiceDateState
                        "Nama Pelanggan" -> customerNameState
                        "Alamat Pelanggan" -> customerAddressState
                        "Pajak" -> taxAmountState
                        else -> remember { mutableStateOf("") }
                    }

                    InputFieldWithLabel(label = label, textState = textState)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tambahkan tabel di bawah alamat pelanggan
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemTable(context)
                }

                item {
                    DatePickerButton()                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    StatusSelector(
                        selectedStatus = selectedStatusState.value,
                        onStatusSelected = { newStatus -> selectedStatusState.value = newStatus }
                    )
                }

                // Menambahkan ActionButtons di dalam LazyColumn
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ActionButtons()
                    Spacer(modifier = Modifier.height(16.dp)) // Memberikan jarak setelah tombol
                }
            }
        }
    }
}

@Composable
fun DatePickerButton() {
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf("") }

    // Fungsi untuk menampilkan DatePickerDialog
    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Mengupdate nilai state dengan tanggal yang dipilih
                val formattedDate = "${dayOfMonth}/${month + 1}/$year"
                selectedDate.value = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    // Button yang menampilkan ikon dan membuka DatePicker saat diklik
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Tanggal Invoice: ${selectedDate.value}",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        IconButton(onClick = { showDatePickerDialog() }) {
            Icon(
                painter = painterResource(id = R.drawable.tanggal_btn),  // Menggunakan drawable tanggal_btn
                contentDescription = "Pilih Tanggal"
            )
        }
    }
}





@Composable
fun ItemTable(context: Context) {
    val items = remember { mutableStateListOf<Pair<String, String>>() }
    var showPopup by remember { mutableStateOf(false) } // State untuk mengontrol visibilitas popup

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(1.dp, Color.Gray), // Menambahkan stroke dengan ketebalan 1dp dan warna abu-abu
                shape = RoundedCornerShape(16.dp) // Bentuk stroke mengikuti bentuk RoundedCornerShape dari Column
            )
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Text(
            text = "Daftar item",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        Divider(color = Color.Gray, thickness = 0.5.dp)

        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(0.1f)
                )
                Text(
                    text = item.first,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(0.5f)
                )
                Text(
                    text = item.second,
                    color = Color(0xFF7B61FF),
                    fontSize = 16.sp,
                    modifier = Modifier.weight(0.3f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "More Options",
                    modifier = Modifier
                        .size(24.dp)
                        .weight(0.1f)
                        .clickable {
                            // Aksi untuk ikon titik tiga
                        }
                )
            }
            Divider(color = Color.Gray, thickness = 0.5.dp)
        }

        // Tombol untuk menambah item baru
        Text(
            text = "+ Tambah item",
            color = Color(0xFF7B61FF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp)
                .clickable {
                    showPopup = true // Tampilkan popup saat diklik
                }
        )

        // Tampilkan popup jika showPopup bernilai true
        if (showPopup) {
            ItemListPopup(
                items = items,
                onClose = { showPopup = false },
                onItemAdded = { newItem ->
                    items.add(newItem)
                }
            )
        }

    }
}

fun saveInvoiceToFirestore(
    alamatPelanggan: String,
    alamatPerusahaan: String,
    catatan: String,
    item: List<String>,
    jatuhTempo: Timestamp,
    namaPelanggan: String,
    namaPerusahaan: String,
    nominal: Long,
    nomorInvoice: String,
    status: String,
    tanggalInvoice: Timestamp
) {
    val firestore = FirebaseFirestore.getInstance()
    val invoiceData = hashMapOf(
        "alamat pelanggan" to alamatPelanggan,
        "alamat perusahaan" to alamatPerusahaan,
        "catatan" to catatan,
        "item" to item,
        "jatuh tempo" to jatuhTempo,
        "nama pelanggan" to namaPelanggan,
        "nama perusahaan" to namaPerusahaan,
        "nominal" to nominal,
        "nomor invoice" to nomorInvoice,
        "status" to status,
        "tanggal invoice" to tanggalInvoice
    )

    firestore.collection("invoice").add(invoiceData)
        .addOnSuccessListener { documentReference ->
            println("Invoice berhasil disimpan dengan ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            println("Gagal menyimpan invoice: $e")
        }
}


@Composable
fun ActionButtons() {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    // State diambil dari konteks yang ada di AddInvoiceActivity agar konsisten
    val companyName = remember { mutableStateOf("") }
    val companyAddress = remember { mutableStateOf("") }
    val invoiceNumber = remember { mutableStateOf("") }
    val invoiceDate = remember { mutableStateOf("") }
    val customerName = remember { mutableStateOf("") }
    val customerAddress = remember { mutableStateOf("") }
    val taxAmount = remember { mutableStateOf("") }
    val items = remember { mutableStateListOf<Pair<String, String>>() }
    val selectedStatus = remember { mutableStateOf("Lunas") }
    var logoBitmap: Bitmap? = null // Placeholder untuk logo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tombol Simpan
        Button(
            onClick = {
                // Validasi data sebelum memanggil addInvoiceToFirestore
                if (companyName.value.isNotEmpty() &&
                    companyAddress.value.isNotEmpty() &&
                    invoiceNumber.value.isNotEmpty() &&
                    invoiceDate.value.isNotEmpty() &&
                    customerName.value.isNotEmpty() &&
                    customerAddress.value.isNotEmpty() &&
                    taxAmount.value.isNotEmpty() &&
                    items.isNotEmpty()
                ) {
                    // Panggil fungsi untuk menambahkan data ke Firestore
                    saveInvoiceToFirestore(
                        alamatPelanggan = customerAddress.value,
                        alamatPerusahaan = companyAddress.value,
                        catatan = "konsultasi limbah", // Sesuaikan jika ada input field catatan
                        item = items.map { it.second }, // Hanya mengambil nama item
                        jatuhTempo = Timestamp.now(), // Menggunakan Timestamp yang benar
                        namaPelanggan = customerName.value,
                        namaPerusahaan = companyName.value,
                        nominal = taxAmount.value.toLongOrNull() ?: 0L, // Menggunakan Long
                        nomorInvoice = invoiceNumber.value,
                        status = selectedStatus.value,
                        tanggalInvoice = Timestamp.now() // Menggunakan Timestamp yang benar
                    )


                    // Panggil fungsi generateInvoicePdf jika dibutuhkan
//                    generateInvoicePdf(
//                        context = context,
//                        logoBitmap = logoBitmap,  // Pastikan ini valid
//                        companyName = companyName.value,
//                        companyAddress = companyAddress.value,
//                        invoiceNumber = invoiceNumber.value,
//                        invoiceDate = invoiceDate.value,
//                        customerName = customerName.value,
//                        customerAddress = customerAddress.value,
//                        taxAmount = taxAmount.value,
//                        items = items,
//                        selectedStatus = selectedStatus.value
//                    )

                } else {
                    // Menampilkan pesan error jika data tidak lengkap
                    Toast.makeText(context, "Lengkapi semua data sebelum menyimpan.", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00549C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Simpan",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Bagikan
        Button(
            onClick = {
                // Tambahkan aksi untuk tombol bagikan
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7DFFB1)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Bagikan",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}





@Composable
fun ItemListPopup(
    items: MutableList<Pair<String, String>>,
    onClose: () -> Unit,
    onItemAdded: (Pair<String, String>) -> Unit
) {
    var userInputName by remember { mutableStateOf("") }
    var userInputPrice by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onClose,
        confirmButton = {
            TextButton(onClick = onClose) { Text("Tutup") }
        },
        title = { Text(text = "Tambah Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = userInputName,
                    onValueChange = { userInputName = it },
                    label = { Text("Nama Item") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userInputPrice,
                    onValueChange = { userInputPrice = it },
                    label = { Text("Nominal") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (userInputName.isNotEmpty() && userInputPrice.isNotEmpty()) {
                            val newItem = Pair(userInputName, userInputPrice)
                            onItemAdded(newItem)
                            userInputName = ""
                            userInputPrice = ""
                            onClose() // Menutup dialog setelah item berhasil ditambahkan
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Tambah")
                }
            }
        }
    )
}

@Composable
fun StatusSelector(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val statusList = listOf("Lunas", "Segera", "Jatuh tempo", "Final", "Dikirim", "Disetujui")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Menambahkan label "Stampel" di atas status-selector
        Text(
            text = "Stampel",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp) // Memberi padding agar tidak terlalu dekat
        )

        Spacer(modifier = Modifier.height(8.dp)) // Memberikan jarak antara label dan row

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(22.dp), // Mengatur jarak antar item
            verticalAlignment = Alignment.CenterVertically
        ) {
            statusList.forEach { status ->
                // Memilih warna berdasarkan status terpilih
                val color = if (status == selectedStatus) Color(0xFF8AD992) else Color(0xFFD1D1D1)

                // Menggunakan Column untuk meletakkan lingkaran dan label di bawahnya
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onStatusSelected(status) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp) // Ukuran box untuk menyimpan lingkaran
                            .clip(CircleShape)
                            .background(color),
                        contentAlignment = Alignment.Center
                    ) {
                        // Lingkaran kosong tanpa teks di dalamnya
                    }

                    // Menampilkan label di bawah lingkaran
                    Text(
                        text = status,
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}



@Composable
fun InputFieldWithLabel(label: String, textState: MutableState<String>) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // Menjaga rounded corners
                    .height(50.dp)
                    .background(Color.White) // Latar belakang putih
                    .border(
                        BorderStroke(1.dp, Color.Gray), // Menambahkan stroke dengan ketebalan 1dp dan warna abu-abu
                        shape = RoundedCornerShape(8.dp) // Bentuk rounded corners sesuai background
                    ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent, // Nonaktifkan background
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

        }
    }
}

fun generateInvoicePdf(
    context: Context,
    logoBitmap: Bitmap?,
    companyName: String,
    companyAddress: String,
    invoiceNumber: String,
    invoiceDate: String,
    customerName: String,
    customerAddress: String,
    taxAmount: String,
    items: List<Pair<String, String>>,
    selectedStatus: String
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()
    val titlePaint = Paint().apply {
        isFakeBoldText = true
        textSize = 20f
    }

    // Menambahkan logo
    logoBitmap?.let {
        canvas.drawBitmap(it, 50f, 40f, paint)
    }
    

    // Informasi perusahaan
    canvas.drawText(companyName, 160f, 60f, titlePaint)
    paint.textSize = 12f
    canvas.drawText(companyAddress, 160f, 90f, paint)

    // Informasi Invoice dan Tanggal
    titlePaint.textSize = 16f
    canvas.drawText("INVOICE: $invoiceNumber", 445f, 60f, titlePaint)
    paint.textSize = 12f
    canvas.drawText("Tanggal: $invoiceDate", 445f, 80f, paint)

    // Informasi Pelanggan
    canvas.drawText("Kepada:", 50f, 200f, titlePaint)
    paint.isFakeBoldText = true
    canvas.drawText(customerName, 50f, 220f, paint)
    paint.isFakeBoldText = false
    canvas.drawText(customerAddress, 50f, 240f, paint)

    // Header Tabel Item
    val headerX = 50f
    val headerY = 280f
    canvas.drawText("Item", headerX, headerY, paint)
    canvas.drawText("Harga", headerX + 200, headerY, paint)

    // Isi Tabel Item
    var itemY = headerY + 20
    var totalAmount = 0.0
    items.forEach { (itemName, itemPrice) ->
        canvas.drawText(itemName, headerX, itemY, paint)
        canvas.drawText(itemPrice, headerX + 200, itemY, paint)

        // Konversi harga ke Double untuk menghitung total
        val price = itemPrice.replace("Rp", "").replace(".", "").toDoubleOrNull() ?: 0.0
        totalAmount += price
        itemY += 20
    }

    // Subtotal, PPN, dan Total
    val tax = taxAmount.toDoubleOrNull() ?: 0.0
    val totalWithTax = totalAmount + tax

    // Menambahkan bagian Subtotal dan Total
    canvas.drawText("Subtotal", headerX + 350, itemY + 60, paint)
    canvas.drawText("Rp$totalAmount", headerX + 450, itemY + 60, paint)
    canvas.drawText("PPN $taxAmount%", headerX + 350, itemY + 80, paint)
    canvas.drawText("Rp$tax", headerX + 450, itemY + 80, paint)
    canvas.drawText("Total", headerX + 350, itemY + 120, titlePaint)
    canvas.drawText("Rp$totalWithTax", headerX + 450, itemY + 120, titlePaint)

    pdfDocument.finishPage(page)

    // Simpan ke file
    val filePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "Invoice.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(filePath))
        Toast.makeText(context, "PDF berhasil disimpan di: ${filePath.path}", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        Toast.makeText(context, "Gagal menyimpan PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        pdfDocument.close()
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    FundflowTheme {
        mainStateAddInvoiceActivity()
    }
}
