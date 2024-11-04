package com.example.fundflow.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign

class UtangActifity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundflowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    mainStateUtangActifity(utangList = sampleUtangList(), modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Sample data class for invoices
data class Utang(
    val companyName: String,
    val date: String,
    val service: String,
    val amount: String,
    val status: String
)

@Composable
fun mainStateUtangActifity(utangList: List<Utang>, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        // Gambar latar belakang
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
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ikon kembali
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back Arrow",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Utang",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            if (utangList.isEmpty()) {
                // Tampilkan pesan jika daftar kosong
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_list_empty),
                        contentDescription = "Empty List",
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = "Belum ada utang yang tersimpan",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Text(
                        text = "Tambahkan utang baru untuk memulai",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                // Menampilkan daftar invoice jika tidak kosong
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 96.dp)
                ) {
                    items(utangList) { invoice ->
                        UtangItem(
                            companyName = invoice.companyName,
                            date = invoice.date,
                            service = invoice.service,
                            amount = invoice.amount,
                            status = invoice.status
                        )
                    }
                }
            }

            // Tombol tambah utang
            Button(
                onClick = {
                    // Implementasi navigasi ke AddInvoiceActivity jika diperlukan
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
    service: String,
    amount: String,
    status: String
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = service,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtangActifityPreview() {
    FundflowTheme {
        mainStateUtangActifity(utangList = sampleUtangList())
    }
}

fun sampleUtangList(): List<Utang> {
    return listOf(
        Utang("PT. Gudang Garam Tbk", "4 April 2025", "Jasa Konsultan", "Rp 9.600.000", "Belum Lunas"),
        Utang("PT. Indofood Sukses Makmur", "10 Mei 2025", "Pengadaan", "Rp 12.000.000", "Lunas")
    )
}
