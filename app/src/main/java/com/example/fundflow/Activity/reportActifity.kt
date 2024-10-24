package com.example.fundflow.Activity

import android.app.DatePickerDialog
import android.graphics.Paint
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.fundflow.Activity.ui.theme.*
import com.example.fundflow.Activity.ui.theme.Blue
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.Activity.ui.theme.Purple200
import com.example.fundflow.Activity.ui.theme.Purple500
import com.example.fundflow.Activity.ui.theme.Purple700
import com.example.fundflow.Activity.ui.theme.Teal200
import com.example.fundflow.R
import java.util.Calendar

class reportActifity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundflowTheme {

            }
        }
    }
}
@Composable
fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = 120.dp,
    chartBarWidth: Dp = 16.dp,
    animDuration: Int = 1000,
    spaceBetween: Float = 12f
) {
    // State untuk menyimpan tanggal yang dipilih
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // State untuk mengontrol kapan DatePicker ditampilkan
    var showDatePicker by remember { mutableStateOf(false) }

    // Tanggal yang dipilih
    var selectedDate by remember { mutableStateOf("$day/${month + 1}/$year") }

    // Fungsi untuk menampilkan DatePickerDialog
    if (showDatePicker) {
        val datePickerDialog = DatePickerDialog(
            LocalContext.current,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                showDatePicker = false // Reset state setelah memilih tanggal
            }, year, month, day
        )

        // Tambahkan listener untuk menangani kondisi ketika dialog dibatalkan
        datePickerDialog.setOnCancelListener {
            showDatePicker = false // Reset state jika dialog dibatalkan
        }

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    // Hitung nilai float untuk sudut
    data.values.forEach { value ->
        floatValue.add(360 * value.toFloat() / totalSum.toFloat())
    }

    val colors = listOf(
        Purple200, Purple500, Teal200, Purple700, Blue
    )

    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    // Animasi ukuran dan rotasi
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    // Box with background image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Latar belakang menggunakan drawable aruskasbg
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Icon dan teks di pojok kanan atas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Padding agar tidak terlalu mepet ke pinggir
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()  // Agar Row mengisi lebar penuh
                    .padding(top = 18.dp), // Padding sesuai kebutuhan
                verticalAlignment = Alignment.CenterVertically // Konten sejajar secara vertikal
            ) {
                // Ikon di posisi start
                Image(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back Arrow",
                    modifier = Modifier
                        .size(24.dp)  // Ukuran ikon
                        .align(Alignment.CenterVertically) // Agar ikon sejajar vertikal
                )

                // Spasi di antara ikon dan teks
                Spacer(modifier = Modifier.width(125.dp))

                // Teks di tengah dengan weight
                Text(
                    text = "Laporan",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f) // Ini membuat teks mengisi ruang di antara ikon dan ujung Row
                        .align(Alignment.CenterVertically) // Pastikan teks tetap sejajar secara vertikal
                )
            }

        }

        // Konten chart
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tambahkan teks "Laporan bulan ini" di atas pie chart dengan align kiri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 33.dp, bottom = 20.dp), // Padding untuk penyesuaian posisi
                verticalAlignment = Alignment.CenterVertically, // Mengatur agar gambar dan teks sejajar secara vertikal
                horizontalArrangement = Arrangement.SpaceBetween // Atur agar teks di kiri dan gambar di kanan
            ) {
                // Tambahkan teks "Laporan bulan ini"
                Text(
                    text = "Laporan bulan ini",
                    fontSize = 24.sp,             // Ukuran teks
                    fontWeight = FontWeight.Bold,  // Teks bold
                    color = Color.Black            // Warna teks hitam
                )

                // Tambahkan gambar tanggal di sebelah kanan
                Image(
                    painter = painterResource(id = R.drawable.tanggal_btn),
                    contentDescription = "tanggal",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.CenterVertically)
                        .padding(end = 30.dp, top = 10.dp)
                        .clickable {
                            showDatePicker = true // Ketika tombol diklik, tampilkan DatePicker
                        }
                )
            }

            Box(
                modifier = Modifier.size(animateSize.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(radiusOuter * 2f)
                        .rotate(animateRotation)
                ) {
                    floatValue.forEachIndexed { index, value ->
                        // Hitung start angle dengan offset untuk rounded corners
                        val roundedStartAngle = lastValue + (spaceBetween / 2) // Tambahkan setengah dari space
                        val roundedSweepAngle = value - spaceBetween // Kurangi space untuk sweep angle

                        drawArc(
                            color = colors[index % colors.size],  // handle more categories than colors
                            startAngle = roundedStartAngle,
                            sweepAngle = roundedSweepAngle,
                            useCenter = false,
                            style = Stroke(
                                chartBarWidth.toPx(),
                                cap = StrokeCap.Round
                            ) // Ubah StrokeCap menjadi Round
                        )

                        lastValue += roundedSweepAngle + spaceBetween // Update lastValue
                    }
                }

                // Menambahkan teks di tengah pie chart tanpa animasi
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Teks "Total" dengan warna hex #000192
                    Text(
                        text = "Total Saldo",                   // Teks "Total"
                        fontSize = 24.sp,                 // Ukuran teks
                        color = Color(0xFF000192),        // Warna teks menggunakan kode hex #000192 (biru tua)
                        fontWeight = FontWeight.Medium    // Teks dengan weight sedang
                    )

                    // Teks totalSum dengan warna hex #370665
                    Text(
                        text = "Rp$totalSum",               // Tampilkan nilai totalSum
                        fontSize = 30.sp,                 // Ukuran teks
                        color = Color(0xFF370665),        // Warna teks menggunakan kode hex #370665 (ungu gelap)
                        fontWeight = FontWeight.Bold      // Teks tebal untuk nilai total
                    )
                }
            }

            // Panggil DetailsPieChart
            DetailsPieChart(
                data = data,
                colors = colors
            )
        }
    }
}





@Composable
fun DetailsPieChart(
    data: Map<String, Int>,
    colors: List<Color>
) {
    Box( // Gunakan Box untuk mengontrol posisi teks
        modifier = Modifier
            .fillMaxWidth() // Memastikan Box mengisi lebar penuh
    ) {
        Text(
            text = "Detail Saldo", // Tambahkan teks yang diinginkan
            fontSize = 16.sp,                   // Ukuran teks
            fontWeight = FontWeight.Medium,       // Teks bold
            color = Color(0xFF8F90FF),                // Warna teks
            modifier = Modifier
                .align(Alignment.CenterStart)          // Menyusun teks di sebelah kiri
                .padding(start = 25.dp, top = 50.dp) // Padding agar tidak terlalu mepet dengan Box di bawahnya
        )
    }
    // Menggunakan LazyColumn agar bisa di-scroll
    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        // Memasukkan Text di dalam item {} di LazyColumn


        // Menampilkan setiap item dalam daftar
        itemsIndexed(data.values.toList()) { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index % colors.size] // handle more categories than colors
            )
        }
    }
}


@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = 45.dp,
    color: Color,
    backgroundHeight: Dp = 90.dp
) {
    // Teks di atas Box "Detail Saldo"


    // Box untuk latar belakang dan konten
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(backgroundHeight)
            .padding(vertical = 6.dp, horizontal = 16.dp) // Tambah padding horizontal
    ) {
        // Latar belakang menggunakan drawable background_rect
        Image(
            painter = painterResource(id = R.drawable.background_rect),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )

        // Konten chart item
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kotak warna di sebelah kiri
            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(50.dp)
                    )
                    .size(30.dp) // Sesuaikan ukuran kotak dengan height
            )

            // Teks
            Column(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = data.first,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Text(
                    text = "Rp ${data.second}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun ChartPreview() {
    FundflowTheme {
        val sampleData = mapOf(
            "Category 1" to 40,
            "Category 2" to 30,
            "Category 3" to 20,
            "Category 4" to 10
        )
        PieChart(data = sampleData)
    }
}

