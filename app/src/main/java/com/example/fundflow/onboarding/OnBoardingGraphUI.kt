package com.example.fundflow.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.format.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.fundflow.R


// Definisikan FontFamily untuk Poppins
val poppinsBold = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val poppinsLight = FontFamily(
    Font(R.font.poppins_light, FontWeight.Light)
)

val poppinsMedium = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium)
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnBoardingGraphUI(onBoardingModel: OnBoardingModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = onBoardingModel.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Menggunakan 60% ruang untuk gambar
                .padding(bottom = 8.dp) // Jarak lebih kecil antara gambar dan teks
        )

        // Judul dengan font Poppins Bold
        Text(
            text = onBoardingModel.title,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = poppinsBold,  // Menggunakan Poppins Bold
                fontSize = 34.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // Menggunakan ukuran dinamis untuk tinggi teks
                .padding(bottom = 4.dp) // Jarak kecil antara judul dan deskripsi
        )

        // Deskripsi dengan font Poppins Light
        Text(
            text = onBoardingModel.description,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = poppinsLight,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // Menggunakan ukuran dinamis untuk tinggi teks deskripsi
        )
    }
}



