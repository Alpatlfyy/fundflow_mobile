package com.example.fundflow.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R

class AboutActivity : ComponentActivity() {
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
                            HeaderRow() // Header dengan tombol kembali dan judul
                            Greeting(
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
fun HeaderRow() {
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
            text = "Tentang Aplikasi",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val textContent = "**Pengertian Aplikasi Mobile untuk Manajemen Keuangan**  \n" +
            "Aplikasi mobile untuk manajemen keuangan adalah alat yang dirancang untuk membantu individu atau perusahaan mengelola dan memantau arus kas mereka dengan lebih efisien. Dengan aplikasi ini, pengguna dapat dengan mudah mencatat pemasukan, pengeluaran, serta memantau catatan kas akhir mereka secara real-time, sehingga memungkinkan mereka untuk membuat keputusan keuangan yang lebih baik dan lebih cepat.  \n" +
            "\n" +
            "**Pengelolaan Keuangan dalam Perusahaan**  \n" +
            "Pengelolaan keuangan kas yang baik sangat penting bagi kelangsungan suatu perusahaan. Kas yang dikelola dengan baik memungkinkan perusahaan untuk mengalokasikan dana dengan bijak, memaksimalkan pendapatan, serta meminimalkan risiko finansial. Aplikasi mobile yang dikembangkan tidak hanya mencatat transaksi secara otomatis tetapi juga memungkinkan pengguna untuk menyusun laporan keuangan yang dibutuhkan untuk pengambilan keputusan strategis. Hal ini sangat penting terutama untuk perusahaan yang ingin mengelola operasional mereka dengan efisien dan menghindari kesalahan yang bisa terjadi akibat pengelolaan keuangan yang kurang baik.  \n" +
            "\n" +
            "Dengan aplikasi yang menawarkan kemudahan dalam pengelolaan keuangan, perusahaan dapat lebih fokus pada pengembangan layanan dan proyek yang ditawarkan, sementara aplikasi secara otomatis membantu dalam pemantauan keuangan, perencanaan, dan pengendalian."

    Text(
        text = formatTextWithBold(textContent),
        modifier = modifier.padding(horizontal = 16.dp),
        color = Color.Black
    )
}

fun formatTextWithBold(input: String): AnnotatedString {
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
fun AboutActivityPreview() {
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
                HeaderRow()
                Greeting(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
