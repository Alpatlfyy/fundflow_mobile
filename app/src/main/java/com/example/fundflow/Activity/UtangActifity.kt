package com.example.fundflow.Activity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.runtime.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale

class UtangActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        firestore = Firebase.firestore


        // Mengambil data dari Firestore dan mengisi utangList
        setContent {
            val utangList = remember { mutableStateListOf<Utang>() }

            // Mengambil data dari Firestore dalam coroutine
            LaunchedEffect(Unit) {
                getUtang { utangs ->
                    utangList.clear()
                    utangList.addAll(utangs)
                }
            }

            FundflowTheme {
                mainStateUtangListActivity(
                    utangList = utangList,
                    onDelete = { id ->
                        deleteUtang(
                            id,
                            onSuccess = {
                                utangList.removeIf { it.id == id }
                            },
                            onFailure = { exception ->
                                println("Error deleting document: $exception")
                            }
                        )
                    },
                    onMarkAsPaid = { id ->
                        markAsPaid(
                            id,
                            onSuccess = {
                                val index = utangList.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    utangList[index] = utangList[index].copy(status = "Lunas")
                                }
                            },
                            onFailure = { exception ->
                                println("Error updating status: $exception")
                            }
                        )
                    }
                )
            }
        }
    }


    private fun getUtang(onResult: (List<Utang>) -> Unit) {
        firestore.collection("utang piutang")
            .whereEqualTo("jenis", "utang")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("Error listening to updates: $exception")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val utangs = snapshot.documents.map { document ->
                        Utang(
                            id = document.id, // Ambil ID dokumen
                            companyName = document.getString("keterangan") ?: "Unknown",
                            date = document.getTimestamp("jatuh tempo")?.toDate()?.toString() ?: "N/A",
                            service = document.getString("keterangan") ?: "N/A",
                            amount = formatRupiah(document.getDouble("nominal") ?: 0.0),
                            status = document.getString("status")?: "N/A"
                        )
                    }
                    onResult(utangs) // Kirimkan data yang diperbarui ke callback
                }
            }
    }


    private fun deleteUtang(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("utang piutang")
            .document(id) // Hapus berdasarkan ID dokumen
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    private fun markAsPaid(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firestore.collection("utang piutang")
            .document(id)
            .update("status", "Lunas") // Memperbarui field status menjadi "Lunas"
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


}

//fun formatRupiah(amount: Double): String {
//    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
//    return format.format(amount).replace("Rp", "Rp ")
//}

// Sample data class for utang
data class Utang(
    val id: String, // Tambahkan ID dokumen
    val companyName: String,
    val date: String,
    val service: String,
    val amount: String,
    val status: String
)


@Composable
fun mainStateUtangListActivity(
    utangList: List<Utang>,
    onDelete: (String) -> Unit,
    onMarkAsPaid: (String) -> Unit

) {
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
                text = "Utang",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f) // Ini membuat teks mengisi ruang di antara ikon dan ujung Row
                    .align(Alignment.CenterVertically) // Pastikan teks tetap sejajar secara vertikal
            )
        }

        // Jika daftar utang kosong, tampilkan gambar 'ic_list_empty'
        if (utangList.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center), // Menempatkan kolom di tengah layar
                horizontalAlignment = Alignment.CenterHorizontally // Menyelaraskan konten di tengah secara horizontal
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_list_empty),
                    contentDescription = "Empty List",
                    modifier = Modifier.size(150.dp) // Mengatur ukuran gambar
                )
                Text(
                    text = "Belum ada kategori yang tersimpan",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp) // Memberikan jarak antara gambar dan teks
                )
                Text(
                    text = "Tambahkan Utang baru untuk memulai menampilkan data Anda",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp) // Memberikan jarak antara gambar dan teks
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 46.dp, bottom = 46.dp) // Menambahkan padding agar tidak menutupi header
            ) {
                items(utangList) { utang ->
                        UtangItem(
                            companyName = utang.companyName,
                            date = utang.date,
                            amount = utang.amount,
                            status = utang.status,
                            onDelete = {
                                deleteTargetId = utang.id
                                showDeleteDialog = true
                            },
                                onMarkAsPaid = {
                                    onMarkAsPaid(utang.id)
                                }
                        )
                    }
                }
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

        Button(
            onClick = {
                val intent = Intent(context, AddUtangActivity::class.java)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00549C)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Tambah Utang",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun UtangItem(
    companyName: String,
    date: String,
    amount: String,
    status: String,
    onDelete: () -> Unit, // Tambahkan parameter untuk aksi delete
    onMarkAsPaid: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_rect),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
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
                Spacer(modifier = Modifier.height(39.dp)) // Spasi sebelum tombol
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp) // Spasi antara tombol
                ) {
                    // Tombol Hapus
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

                    // Tombol Lunas
                    Button(
                        onClick = onMarkAsPaid,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)), // Warna Orange
                        modifier = Modifier.size(width = 79.dp, height = 30.dp)
                    ) {
                        Text(
                            text = "Lunas",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UtangListPreview() {
//    mainStateUtangListActivity(utangList = sampleUtangList())
}

fun sampleUtangList(): List<Utang> {
    return listOf(
//        Utang("PT. Gudang Garam Tbk", "4 April 2025", "Jasa Konsultan", "Rp 9.600.000", "Belum Lunas"),
//        Utang("PT. Indofood Sukses Makmur", "10 Mei 2025", "Pengadaan", "Rp 12.000.000", "Lunas")
    )
}
