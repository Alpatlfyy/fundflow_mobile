package com.example.fundflow.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .padding(vertical = 12.dp, horizontal = 24.dp),
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
            .background(Color.Gray) // Ganti dengan warna sesuai kebutuhan
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_google), // Ganti dengan ID drawable logo Google
                contentDescription = null, // Deskripsi untuk aksesibilitas
                modifier = Modifier.size(24.dp) // Atur ukuran logo
            )
            Spacer(modifier = Modifier.width(8.dp)) // Jarak antara logo dan teks
            Text(
                text = "Login with Google",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun LoginButtonsRow(
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp), // Spasi antara tombol
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Pastikan tombol berada di tengah
    ) {
        ButtonUi(
            text = "Login",
            backgroundColor = Color(0xFF3F40FC),
            textColor = Color.White,
            fontSize = 16,
            onClick = onLoginClick
        )
        GoogleLoginButton(onClick = onGoogleLoginClick, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun LoginButtonsRowPreview() {
    LoginButtonsRow(
        onLoginClick = {},
        onGoogleLoginClick = {}
    )
}
