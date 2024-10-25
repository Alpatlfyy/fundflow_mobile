package com.example.fundflow.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // For layout components
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.* // Material3 for UI components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.fundflow.ui.theme.FundflowTheme
import com.example.fundflow.R

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FundflowTheme {
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Profile", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back button */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box {
                // Gambar latar belakang utama
                Image(
                    painter = painterResource(id = R.drawable.background_top_main),
                    contentDescription = "Background Top Main",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                // Konten profil
                ProfileContent()
            }
        }
    }
}

@Composable
fun ProfileContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 160.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar profil
        Image(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        // Informasi profil
        Text(
            text = "Nama",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Kategori",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 1.dp)
        )
        Text(
            text = "emailexample@email.com",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 1.dp, bottom = 50.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Menu items
        MenuItem(iconResId = R.drawable.loc_icon, label = "Alamat")
        MenuItem(iconResId = R.drawable.data_icon, label = "Ubah data pribadi")
        MenuItem(iconResId = R.drawable.lock_icon, label = "Ubah password")
        MenuItem(iconResId = R.drawable.inf_icon, label = "Tentang aplikasi")
        MenuItem(iconResId = R.drawable.sup_icon, label = "Bantuan")
    }
}

@Composable
fun MenuItem(iconResId: Int, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 38.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text label
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FundflowTheme {
        ProfileScreen()
    }
}
