package com.example.fundflow.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class AddInvoiceActifity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundflowTheme {
                mainStateAddInvoiceActifity()
            }
        }
    }
}


@Composable
fun mainStateAddInvoiceActifity() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val selectedMediaUri = data.data
            // Lakukan sesuatu dengan media yang dipilih, misalnya tampilkan di UI atau simpan
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Menambahkan gambar latar belakang
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Mengatur agar gambar mengisi area dengan proporsi yang sesuai
        )

        // Elemen lain di luar LazyColumn
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back Arrow",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            (context as? Activity)?.finish()
                        }
                )

                Spacer(modifier = Modifier.width(108.dp))

                androidx.compose.material.Text(
                    text = "Tambah Invoice",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // LazyColumn untuk elemen yang scrollable
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth() // Hindari fillMaxSize() di LazyColumn jika menyebabkan infinite height
                    .weight(1f)
                    .padding(horizontal = 16.dp)

            ) {

                // Label untuk logo perusahaan
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

                    // Ikon upload media
                    Image(
                        painter = painterResource(id = R.drawable.ic_media_upload),
                        contentDescription = "Upload Media",
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Start)
                            .padding(start = 16.dp)
                            .clickable {
                                // Buka media picker
                                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                launcher.launch(intent)
                            },
                        colorFilter = ColorFilter.tint(Color.Black)
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Input kedua dan seterusnya
                items(listOf("Nama Perusahaan", "Alamat Perusahaan", "Nomor Invoice", "Tanggal Invoice", "Nama Pelanggan", "Alamat Pelanggan")) { label ->
                    InputFieldWithLabel(label)
                    Spacer(modifier = Modifier.height(8.dp)) // Spasi antara setiap InputField
                }
            }
        }
    }
}





// Fungsi untuk membuat TextField dengan label
@Composable
fun InputFieldWithLabel(label: String) {
    val textState = remember { mutableStateOf("") } // State untuk menyimpan input

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp) // Padding top hanya diterapkan pada Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Padding horizontal untuk seluruh grup
        ) {
            // Label di atas TextField
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .padding(bottom = 4.dp) // Padding di bawah label
            )

            // TextField untuk input
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent, // Menghilangkan garis bawah saat fokus
                    unfocusedIndicatorColor = Color.Transparent, // Menghilangkan garis bawah saat tidak fokus
                    focusedContainerColor = Color.White, // Latar belakang saat fokus
                    unfocusedContainerColor = Color.White, // Latar belakang saat tidak fokus
                    focusedTextColor = Color.Black, // Warna teks saat fokus
                    unfocusedTextColor = Color.Black // Warna teks saat tidak fokus
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(80.dp)) // Membulatkan sudut
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddInvoiceActifityPreview() {
    FundflowTheme {
        mainStateAddInvoiceActifity()
    }
}
