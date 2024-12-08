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

class PiutangActivity : ComponentActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        firestore = Firebase.firestore


        // Mengambil data dari Firestore dan mengisi piutangList
        setContent {
            val piutangList = remember { mutableStateListOf<Piutang>() }

            // Mengambil data dari Firestore dalam coroutine
            LaunchedEffect(Unit) {
                getPiutang { piutangs ->
                    piutangList.clear()
                    piutangList.addAll(piutangs)
                }
            }

            FundflowTheme {
                mainStatePiutangListActivity(
                    piutangList = piutangList,
                    onDelete = { id ->
                        deletePiutang(
                            id,
                            onSuccess = {
                                // Hapus item dari daftar lokal
                                piutangList.removeIf { it.id == id }
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
                                val index = piutangList.indexOfFirst { it.id == id }
                                if (index != -1) {
                                    piutangList[index] = piutangList[index].copy(status = "Lunas")
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


    private fun getPiutang(onResult: (List<Piutang>) -> Unit) {
        firestore.collection("utang piutang")
            .whereEqualTo("jenis", "piutang")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("Error listening to updates: $exception")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val piutangs = snapshot.documents.map { document ->
                        Piutang(
                            id = document.id, // Ambil ID dokumen
                            companyName = document.getString("keterangan") ?: "Unknown",
                            date = document.getTimestamp("jatuh tempo")?.toDate()?.toString() ?: "N/A",
                            service = document.getString("keterangan") ?: "N/A",
                            amount = formatRupiah(document.getDouble("nominal") ?: 0.0),
                            status = "Belum Lunas"
                        )
                    }
                    onResult(piutangs) // Kirimkan data yang diperbarui ke callback
                }
            }
    }


    private fun deletePiutang(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
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

// Sample data class for piutang
data class Piutang(
    val id: String, // Tambahkan ID dokumen
    val companyName: String,
    val date: String,
    val service: String,
    val amount: String,
    val status: String
)


@Composable
fun mainStatePiutangListActivity(
    piutangList: List<Piutang>,
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
                text = "Piutang",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f) // Ini membuat teks mengisi ruang di antara ikon dan ujung Row
                    .align(Alignment.CenterVertically) // Pastikan teks tetap sejajar secara vertikal
            )
        }

        // Jika daftar piutang kosong, tampilkan gambar 'ic_list_empty'
        if (piutangList.isEmpty()) {
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
                    text = "Tambahkan Piutang baru untuk memulai menampilkan data Anda",
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
                items(piutangList) { piutang ->
                    PiutangItem(
                        companyName = piutang.companyName,
                        date = piutang.date,
                        amount = piutang.amount,
                        status = piutang.status,
                        onDelete = {
                            deleteTargetId = piutang.id
                            showDeleteDialog = true
                        },
                        onMarkAsPaid = {
                            onMarkAsPaid(piutang.id)
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
                val intent = Intent(context, AddPiutangActivity::class.java)
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
                text = "Tambah Piutang",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PiutangItem(
    companyName: String,
    date: String,
    amount: String,
    status: String,
    onDelete: () -> Unit,
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
                Spacer(modifier = Modifier.height(8.dp)) // Spasi sebelum tombol
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    Button(
                        onClick = onMarkAsPaid, // Callback dipanggil di sini
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier.size(width = 78.dp, height = 30.dp)
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
fun PiutangListPreview() {
//    mainStatePiutangListActivity(piutangList = samplePiutangList())
}

fun samplePiutangList(): List<Piutang> {
    return listOf(
//        Piutang("PT. Gudang Garam Tbk", "4 April 2025", "Jasa Konsultan", "Rp 9.600.000", "Belum Lunas"),
//        Piutang("PT. Indofood Sukses Makmur", "10 Mei 2025", "Pengadaan", "Rp 12.000.000", "Lunas")
    )
}
