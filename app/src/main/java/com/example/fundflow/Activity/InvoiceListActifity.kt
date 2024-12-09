package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R
import com.example.fundflow.UserSingleton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale

class InvoiceListActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore

        // Mengambil data dari Firestore dan mengisi invoiceList
        setContent {
            val invoiceList = remember { mutableStateListOf<Invoice>() }

            // Mengambil data dari Firestore dalam coroutine
            LaunchedEffect(Unit) {
                getInvoices { invoices ->
                    invoiceList.clear()
                    invoiceList.addAll(invoices)
                }
            }

            FundflowTheme {
                mainStateInvoiceListActivity(
                invoiceList = invoiceList,
                onDelete = { id ->
                    deleteInvoice(
                        id,
                        onSuccess = {
                            // Hapus item dari daftar lokal
                            invoiceList.removeIf { it.id == id }

                        },
                        onFailure = { exception ->
                            println("Error deleting document: $exception")
                        }
                    )
                }
                )
            }
        }
    }

    private fun getInvoices(onResult: (List<Invoice>) -> Unit) {
        val currentUid = UserSingleton.getUid()
        firestore.collection("invoice")
            .whereEqualTo("userid",currentUid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("Error listening to updates: $exception")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val invoices = snapshot.documents.map { document ->
                        Invoice(
                            id = document.id, // Ambil ID dokumen
                            companyName = document.getString("nama perusahaan") ?: "Unknown",
                            date = document.getTimestamp("jatuh tempo")?.toDate()?.toString() ?: "N/A",
                            service = document.getString("keterangan") ?: "N/A",
                            amount = formatRupiah(document.getDouble("nominal") ?: 0.0),
                            status = document.getString("status")?:"N/A"
                        )
                    }
                    onResult(invoices) // Kirimkan data yang diperbarui ke callback
                }
            }
    }

    private fun deleteInvoice(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("invoice")
            .document(id) // Hapus berdasarkan ID dokumen
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}

fun formatRupiah(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(amount).replace("Rp", "Rp ")
}

// Sample data class for invoices
data class Invoice(
    val id: String = "",
    val companyName: String,
    val date: String,
    val service: String,
    val amount: String,
    val status: String
)

@Composable
fun mainStateInvoiceListActivity(invoiceList: List<Invoice>,
                                 onDelete: (String) -> Unit) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteTargetId by remember { mutableStateOf("") }

    Column {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Padding agar tidak terlalu mepet ke pinggir
    ) {
        // Header Row (Back Arrow + Title)
        Row(
            modifier = Modifier
                .fillMaxWidth()  // Agar Row mengisi lebar penuh
                .padding(top = 4.dp), // Padding sesuai kebutuhan
            verticalAlignment = Alignment.CenterVertically // Konten sejajar secara vertikal
        ) {
            // Ikon di posisi start
            Image(
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "Back Arrow",
                modifier = Modifier
                    .size(24.dp)  // Ukuran ikon
                    .align(Alignment.CenterVertically) // Agar ikon sejajar vertikal
                    .clickable { (context as? ComponentActivity)?.finish() }
            )

            // Spasi di antara ikon dan teks
            Spacer(modifier = Modifier.width(125.dp))

            // Teks di tengah dengan weight
            Text(
                text = "Invoice",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f) // Ini membuat teks mengisi ruang di antara ikon dan ujung Row
                    .align(Alignment.CenterVertically) // Pastikan teks tetap sejajar secara vertikal
            )
        }

        // Jika daftar invoice kosong, tampilkan gambar 'ic_list_empty'
        if (invoiceList.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center), // Menempatkan kolom di tengah layar
                horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan konten di tengah secara horizontal
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_list_empty),
                    contentDescription = "Empty List",
                    modifier = Modifier.size(150.dp) // Mengatur ukuran gambar
                )

                // Teks di bawah gambar
                Text(
                    text = "Belum ada kategori yang tersimpan",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp) // Memberikan jarak antara gambar dan teks
                )
                Text(
                    text = "Tambahkan Invoice baru untuk memulai menampilkan data Anda",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp) // Memberikan jarak antara gambar dan teks
                )
            }

        } else {
            // Menampilkan daftar invoice jika tidak kosong
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 46.dp, bottom = 46.dp) // Menambahkan padding agar tidak menutupi header
            ) {
                items(invoiceList) { invoice ->
                    InvoiceItem(
                        companyName = invoice.companyName,
                        date = invoice.date,
                        amount = invoice.amount,
                        status = invoice.status,
                        onDelete = {
                            deleteTargetId = invoice.id
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        // Tombol tambah invoice
        Button(
            onClick = {
                val intent = Intent(context, AddInvoiceActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00549C)), // Warna tombol
            modifier = Modifier
                .align(Alignment.BottomCenter) // Menempatkan tombol di bawah dan tengah
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Tambah Invoice",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(text = "Konfirmasi Hapus")
                },
                text = {
                    Text(text = "Apakah Anda yakin ingin menghapus data ini?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDelete(deleteTargetId)
                            showDeleteDialog = false
                            Toast.makeText(
                                context,
                                "Data berhasil dihapus",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun InvoiceItem(
    companyName: String,
    date: String,
    amount: String,
    status: String,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Membuat sudut rounded
            .background(Color.White) // Atau gunakan drawable background_rect
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_rect),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp)) // Membuat image juga rounded
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = companyName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = amount,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Blue
                )
                Text(
                    text = status,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.size(width = 78.dp, height = 30.dp)
                ) {
                    Text(
                        text = "Hapus",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InvoiceListPreview() {
//    mainStateInvoiceListActivity(invoiceList = sampleInvoiceList())
}

fun sampleInvoiceList(): List<Invoice> {
    return listOf(
        // Invoice("PT. Gudang Garam Tbk", "4 April 2025", "Jasa Konsultan", "Rp 9.600.000", "Belum Lunas"),
        // Invoice("PT. Indofood Sukses Makmur", "10 Mei 2025", "Pengadaan", "Rp 12.000.000", "Lunas")
    )
}
