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
import android.graphics.Color.BLACK
import android.graphics.Color.RED
import android.graphics.Color.WHITE
import android.graphics.Color.rgb
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.widget.Toast
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.google.firebase.Timestamp
import java.util.Calendar


class AddInvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    val itemsState = remember { mutableStateListOf<Pair<String, String>>() }
    val selectedStatusState = remember { mutableStateOf("Lunas") }
    var logoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val catatanstate = remember { mutableStateOf("")}
    val selectedDateState = remember { mutableStateOf<Timestamp?>(null) }
    val selectedTempoState = remember { mutableStateOf<Timestamp?>(null) }
    val nominalstate = remember { mutableStateOf(0L) }



    // State untuk tanggal


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
                    .padding(top = 1.dp)
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

                items(listOf("Nama Perusahaan", "Alamat Perusahaan", "Nomor Invoice", "Nama Pelanggan", "Alamat Pelanggan", "Pajak","Catatan")) { label ->
                    val textState = when (label) {
                        "Nama Perusahaan" -> companyNameState
                        "Alamat Perusahaan" -> companyAddressState
                        "Nomor Invoice" -> invoiceNumberState
                        "Tanggal Invoice" -> invoiceDateState
                        "Nama Pelanggan" -> customerNameState
                        "Alamat Pelanggan" -> customerAddressState
                        "Pajak" -> taxAmountState
                        "Catatan" -> catatanstate
                        else -> remember { mutableStateOf("") }
                    }

                    InputFieldWithLabel(label = label, textState = textState)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tambahkan tabel di bawah alamat pelanggan
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemTable(
                        context = context,
                        items = itemsState,
                        nominal = nominalstate // Mengirimkan nominal yang terupdate
                    )
                }

                item {
                    DatePickerButton(
                        selectedDate = selectedDateState,
                        label = "Tanggal Invoice"
                    )
                }

                // DatePicker untuk tanggal jatuh tempo
                item {
                    DatePickerButton(
                        selectedDate = selectedTempoState,
                        label = "Tanggal Jatuh Tempo"
                    )
                }

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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Tombol Simpan
                        Button(
                            onClick = {


                                saveInvoiceToFirestore(
                                    alamatPelanggan = customerAddressState.value,
                                    alamatPerusahaan = companyAddressState.value,
                                    catatan = catatanstate.value, // Sesuaikan jika ada input field catatan
                                    item = itemsState.toList(), // Hanya mengambil nama item
                                    jatuhTempo = selectedTempoState.value?: Timestamp.now(), // Menggunakan Timestamp yang benar
                                    namaPelanggan = customerNameState.value,
                                    namaPerusahaan = companyNameState.value,
                                    nominal = nominalstate.value, // Menggunakan Long
                                    nomorInvoice = invoiceNumberState.value,
                                    status = selectedStatusState.value, // Menggunakan status terpilih
                                    tanggalInvoice = selectedDateState.value?: Timestamp.now() // Menggunakan Timestamp yang benar
                                )

                                generateInvoicePdf(
                                    context = context,
                                    logoBitmap = logoBitmap, // Menggunakan logoBitmap yang sudah di-assign
                                    companyName = companyNameState.value,
                                    companyAddress = companyAddressState.value,
                                    invoiceNumber = invoiceNumberState.value,
                                    invoiceDate = invoiceDateState.value,
                                    customerName = customerNameState.value,
                                    customerAddress = customerAddressState.value,
                                    taxAmount = taxAmountState.value,
                                    items = itemsState.toList(), // Menggunakan itemsState yang berisi daftar item
                                    selectedStatus = selectedStatusState.value
                                )
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
                    Spacer(modifier = Modifier.height(16.dp)) // Memberikan jarak setelah tombol
                }
            }
        }
    }
}








@Composable
fun ItemTable(
    context: Context,
    items: MutableList<Pair<String, String>>,
    nominal: MutableState<Long> // Menerima nominal sebagai MutableState<Long>
) {
    var showPopup by remember { mutableStateOf(false) }

    // Menghitung nominal berdasarkan harga setiap item
    nominal.value = items.sumOf {
        it.second.toLongOrNull() ?: 0L
    }
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
    item: List<Pair<String, String>>,
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
fun DatePickerButton(
    selectedDate: MutableState<Timestamp?>, // Mengupdate parameter yang diterima
    label: String // Label untuk membedakan tombol (Invoice / Tempo)
) {
    val context = LocalContext.current

    // Fungsi untuk menampilkan DatePickerDialog
    fun showDatePickerDialog(onDateSelected: (Timestamp) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // Update state dengan nilai Timestamp dari tanggal yang dipilih
                calendar.set(year, month, dayOfMonth)
                onDateSelected(Timestamp(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$label: ${selectedDate.value?.toDate()?.toString() ?: "Belum dipilih"}",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        IconButton(
            onClick = {
                showDatePickerDialog { timestamp ->
                    selectedDate.value = timestamp
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.tanggal_btn), // Ikon tombol
                contentDescription = "Pilih Tanggal"
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
    items: List<Pair<String, String>>, // Menggunakan Pair untuk item dan harga
    selectedStatus: String
) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Setup paint untuk berbagai gaya teks
    val regularPaint = Paint().apply {
        textSize = 12f
        color = BLACK
    }

    val titlePaint = Paint().apply {
        textSize = 24f
        isFakeBoldText = true
        color = rgb(82, 71, 157) // Warna ungu sesuai design
    }

    val headerPaint = Paint().apply {
        textSize = 14f
        isFakeBoldText = true
        color = WHITE
    }

    val tablePaint = Paint().apply {
        color = rgb(82, 71, 157) // Warna ungu untuk header tabel
    }

    // Logo
    logoBitmap?.let {
        val widthInPixels = (0.8 * 300 / 2.54).toInt()  // 1.5 cm
        val heightInPixels = (1 * 300 / 2.54).toInt() // 1.5 cm
        val resizedLogoBitmap = Bitmap.createScaledBitmap(it, widthInPixels, heightInPixels, true)
        canvas.drawBitmap(resizedLogoBitmap, 50f, 50f, Paint())
    }

    // Informasi Perusahaan
    canvas.drawText(companyName, 150f, 120f, titlePaint.apply { textSize = 16f })
    canvas.drawText("Kantor Pusat", 150f, 140f, regularPaint.apply { isFakeBoldText = true })
    val addressLines = companyAddress.split(",")
    var yPos = 160f
    addressLines.forEach { line ->
        canvas.drawText(line.trim(), 150f, yPos, regularPaint)
        yPos += 20f
    }

    // Invoice Title dan Tanggal
    canvas.drawText("INVOICE", 400f, 80f, titlePaint)
    canvas.drawText(invoiceDate, 400f, 100f, regularPaint)

    // Informasi Customer
    canvas.drawText("Kepada :", 400f, 140f, regularPaint)
    canvas.drawText(customerName, 400f, 160f, regularPaint.apply { isFakeBoldText = true })
    val customerAddressLines = customerAddress.split(",")
    yPos = 180f
    customerAddressLines.forEach { line ->
        canvas.drawText(line.trim(), 400f, yPos, regularPaint)
        yPos += 20f
    }

    // Tabel Header
    val tableTop = 300f
    val tableLeft = 50f
    val columnWidths = listOf(300f, 115f, 130f) // Item, Harga, Total

    // Draw table header background
    tablePaint.style = Paint.Style.FILL
    canvas.drawRect(tableLeft, tableTop - 30, tableLeft + columnWidths.sum(), tableTop, tablePaint)

    // Draw table headers
    val headers = listOf("Item", "Harga/Item", "Total")
    var xPos = tableLeft
    headers.forEachIndexed { index, header ->
        canvas.drawText(header, xPos + 10, tableTop - 10, headerPaint)
        xPos += columnWidths[index]
    }

    // Draw items
    var currentY = tableTop + 30
    items.forEach { (item, price) ->
        val priceValue = price.replace("Rp", "").replace(".", "").toDoubleOrNull() ?: 0.0

        xPos = tableLeft
        canvas.drawText(item, xPos + 10, currentY, regularPaint)
        canvas.drawText(price, xPos + columnWidths[0] + 10, currentY, regularPaint)
        canvas.drawText(price, xPos + columnWidths[0] + columnWidths[1] + 10, currentY, regularPaint)

        currentY += 40
    }

    // Subtotal, PPN, dan Total
    val subtotal = items.sumOf { (_, price) ->
        price.replace("Rp", "").replace(".", "").toDoubleOrNull() ?: 0.0
    }
    val tax = subtotal * (taxAmount.toDoubleOrNull() ?: 0.0) / 100
    val total = subtotal + tax

    // Draw summary
    val summaryStartX = tableLeft + columnWidths.sum() - 200
    currentY += 20
    canvas.drawText("Subtotal", summaryStartX, currentY, regularPaint)
    canvas.drawText("Rp${String.format("%,.0f", subtotal)}", summaryStartX + 100, currentY, regularPaint)

    currentY += 20
    val ppnPaint = Paint().apply {
        textSize = 12f
        color = RED
    }
    canvas.drawText("PPN ${taxAmount}%", summaryStartX, currentY, ppnPaint)
    canvas.drawText("Rp${String.format("%,.0f", tax)}", summaryStartX + 100, currentY, ppnPaint)

    // Total background
    tablePaint.style = Paint.Style.FILL
    currentY += 40
    canvas.drawRect(summaryStartX - 10, currentY - 20, summaryStartX + 200, currentY + 10, tablePaint)

    // Total text
    canvas.drawText("Total", summaryStartX, currentY, headerPaint)
    canvas.drawText("Rp${String.format("%,.0f", total)}", summaryStartX + 100, currentY, headerPaint)

    pdfDocument.finishPage(page)

    // Save PDF
    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    var filePath = File(directory, "Invoice.pdf")

    var fileCount = 1
    while (filePath.exists()) {
        val newFileName = "Invoice($fileCount).pdf"
        filePath = File(directory, newFileName)
        fileCount++
    }

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
