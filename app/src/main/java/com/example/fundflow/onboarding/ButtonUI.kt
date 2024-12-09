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
            .padding(vertical = 12.dp, horizontal = 16.dp) // Padding untuk memastikan ruang di dalam tombol
            .height(56.dp) // Ukuran tinggi yang konsisten
            .clip(RoundedCornerShape(25.dp)), // Rounded corners
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = textColor, fontSize = fontSize.sp)
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
