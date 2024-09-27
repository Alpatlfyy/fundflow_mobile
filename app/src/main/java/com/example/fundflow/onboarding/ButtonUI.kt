package com.example.fundflow.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.fundflow.R

@Composable
fun ButtonUi(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    fontSize: Int
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp) // Padding untuk memastikan ruang di dalam tombol
            .height(56.dp) // Ukuran tinggi yang konsisten
            .clip(RoundedCornerShape(25.dp)), // Rounded corners
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = fontSize.sp)
    }
}

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
             // Mengubah warna latar belakang menjadi putih
            .border(1.dp, Color.Gray, RoundedCornerShape(25.dp)) // Menambahkan border abu-abu
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(25.dp)), // Rounded corners
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp) // Padding untuk memperbesar ukuran Row
                .height(56.dp), // Atur tinggi Row agar sesuai dengan tombol lainnya
            verticalAlignment = Alignment.CenterVertically, // Menyelaraskan logo dan teks secara vertikal
            horizontalArrangement = Arrangement.Center // Menyelaraskan logo dan teks secara horizontal
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google), // Ganti dengan ID drawable logo Google
                contentDescription = "Login with Google", // Deskripsi untuk aksesibilitas
                modifier = Modifier.size(36.dp) // Atur ukuran logo (lebih besar)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Jarak antara logo dan teks
            Text(
                text = "Login with Google",
                // Ubah warna teks menjadi hitam agar kontras dengan latar belakang putih
                fontSize = 16.sp // Ukuran font
            )
        }
    }
}



@Composable
fun LoginButtonsWithText(
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spasi antara tombol dan teks "atau"
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Menambahkan padding horizontal
        verticalAlignment = Alignment.CenterVertically // Posisikan tombol di tengah
    ) {
        ButtonUi(
            text = "Login",
            backgroundColor = Color(0xFF3F40FC),
            textColor = Color.White,
            fontSize = 16,
            modifier = Modifier
                .weight(1f) // Menggunakan weight untuk membuat tombol lebih fleksibel
                .height(56.dp) // Ukuran tinggi yang konsisten
                .clip(RoundedCornerShape(25.dp)),
            onClick = onLoginClick
        )

        // Text "atau" di antara tombol
        Text(
            text = "atau",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        GoogleLoginButton(
            onClick = onGoogleLoginClick,
            modifier = Modifier
                .weight(1f) // Menggunakan weight untuk membuat tombol lebih fleksibel
                .height(56.dp) // Ukuran tinggi yang konsisten
                .clip(RoundedCornerShape(25.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginButtonsWithTextPreview() {
    LoginButtonsWithText(
        onLoginClick = {},
        onGoogleLoginClick = {}
    )
}
