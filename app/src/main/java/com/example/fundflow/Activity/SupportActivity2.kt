package com.example.fundflow.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R

class SupportActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FundflowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Background Image
                        Image(
                            painter = painterResource(id = R.drawable.background_main),
                            contentDescription = "Background",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Foreground content
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            SupportHeaderRow() // Header dengan tombol kembali dan judul
                            SupportGreeting(
                                modifier = Modifier
                                    .padding(top = 16.dp) // Jarak antar konten
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SupportHeaderRow() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = "Back Arrow",
            modifier = Modifier
                .size(24.dp)
                .clickable { (context as? ComponentActivity)?.finish() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Bantuan",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SupportGreeting(modifier: Modifier = Modifier) {
    val textContent = "** Alur Penggunaan Aplikasi FundFlow**\n" +
            "\n" +
            "Pengguna memulai dengan menentukan apakah sudah memiliki akun di aplikasi FundFlow. Jika belum, mereka akan diarahkan ke formulir registrasi, di mana mereka harus mengisi informasi seperti nama, email, profesi, password, dan konfirmasi password. Setelah melengkapi formulir, pengguna dapat melanjutkan untuk membuat akun.\n" +
            "\n" +
            "Jika pengguna sudah memiliki akun, mereka dapat langsung login menggunakan email dan password yang telah terdaftar. Setelah berhasil login atau registrasi, pengguna akan masuk ke Dashboard utama. Dashboard ini menampilkan informasi penting seperti daftar kategori terbaru, total pemasukan, total pengeluaran, dan kas perusahaan.\n" +
            "\n" +
            "Dari Dashboard, pengguna dapat mengakses berbagai fitur utama, di antaranya:\n" +
            "- **Mencatat arus kas**: Pemasukan dan pengeluaran perusahaan.\n" +
            "- **Mengelola data anggota**: Menambah, menghapus, dan mengedit anggota.\n" +
            "- **Mengelola hutang**: Mencatat dan mengelola hutang perusahaan.\n" +
            "- **Membuat dan menyimpan invoice**: Menghasilkan invoice dalam format PDF.\n" +
            "- **Menulis catatan**: Menyimpan catatan terkait aktivitas perusahaan.\n" +
            "\n" +
            "Selain itu, pengguna dapat melihat laporan kas berdasarkan filter tanggal atau kategori tertentu, menambah kategori pemasukan dan pengeluaran sesuai kebutuhan, serta mengelola profil akun untuk mengubah password atau mendapatkan bantuan.\n" +
            "\n" +
            "Setelah selesai menggunakan aplikasi, pengguna dapat logout untuk mengakhiri sesi, dan proses pun berakhir. Flowchart ini memberikan gambaran menyeluruh tentang cara kerja aplikasi FundFlow, dari awal hingga akhir."

    Text(
        text = SupportFormatTextWithBold(textContent),
        modifier = modifier.padding(horizontal = 16.dp),
        color = Color.Black
    )
}

fun SupportFormatTextWithBold(input: String): AnnotatedString {
    return buildAnnotatedString {
        val boldPattern = Regex("\\*\\*(.*?)\\*\\*")
        var currentIndex = 0

        boldPattern.findAll(input).forEach { match ->
            val matchRange = match.range
            append(input.substring(currentIndex, matchRange.first))

            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
            append(match.groupValues[1])
            pop()

            currentIndex = matchRange.last + 1
        }

        if (currentIndex < input.length) {
            append(input.substring(currentIndex))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SupportGreetingPreview() {
    FundflowTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_main),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                SupportHeaderRow()
                SupportGreeting(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
