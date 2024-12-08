package com.example.fundflow.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // For layout components
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.* // Material3 for UI components
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


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

@Composable
fun FetchUserData(email: String): User1? {
    var user by remember { mutableStateOf<User1?>(null) }
    val firestore = Firebase.firestore

    LaunchedEffect(email) {
        firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    user = User1(
                        name = document.getString("name") ?: "",
                        email = document.getString("email") ?: "",
                        role = document.getString("role") ?: "",
                        username = document.getString("username") ?: ""
                    )
                }
            }
            .addOnFailureListener {
                // Handle errors here
                user = null
            }
    }

    return user
}

data class User1(
    val name: String,
    val email: String,
    val role: String,
    val username: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val context = LocalContext.current

                // Gambar latar belakang utama
                Image(
                    painter = painterResource(id = R.drawable.background_top_main),
                    contentDescription = "Background Top Main",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .zIndex(1f),
                    contentScale = ContentScale.Crop
                )

                // Gambar latar belakang kedua
                Image(
                    painter = painterResource(id = R.drawable.background_cover),
                    contentDescription = "Background Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 200.dp)
                        .zIndex(1f),
                    contentScale = ContentScale.Crop
                )

                // Konten di atas gambar
                ProfileContent()

                // Text and Icon Button in the top-right corner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .zIndex(4f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { (context as? ComponentActivity)?.finish()}) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow), // Use appropriate icon resource
                            contentDescription = "Edit Icon",
                            tint = Color.White
                        )

                    }
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(end = 8.dp),
                        color = Color.White
                    )


                }
            }
        }
    ) { padding ->
        // Gunakan padding yang diteruskan dari Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProfileContent() {
    val context = LocalContext.current
    val email = "alhamdulillah@gmail.com" // Email target
    val user = FetchUserData(email)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 187.dp)
            .zIndex(3f),
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
            text = user?.name ?: "Loading...",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp),
            color =Color.Black

        )
        Text(
            text = user?.role ?: "Loading...",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 1.dp)
        )
        Text(
            text = user?.email ?: "Loading...",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 1.dp, bottom = 50.dp),
            color =Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Add menu items below profile information
//        MenuItem(iconResId = R.drawable.loc_icon, label = "Alamat") {
//            context.startActivity(Intent(context, EditAlamatActivity::class.java))
//        }
        MenuItem(iconResId = R.drawable.data_icon, label = "Ubah data pribadi") {
            context.startActivity(Intent(context, EditDataActivity::class.java))
        }
        MenuItem(iconResId = R.drawable.lock_icon, label = "Ubah password") {
            context.startActivity(Intent(context, EditPasswordActivity::class.java))
        }
        MenuItem(iconResId = R.drawable.inf_icon, label = "Tentang aplikasi") {
            context.startActivity(Intent(context, AboutApp::class.java))
        }
        MenuItem(iconResId = R.drawable.sup_icon, label = "Bantuan") {
            context.startActivity(Intent(context, SupportActivity::class.java))
        }
    }
}

@Composable
fun MenuItem(iconResId: Int, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 38.dp, vertical = 16.dp)
            .clickable { onClick() }, // Tambahkan aksi klik
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
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black // Menetapkan warna teks menjadi hitam
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
