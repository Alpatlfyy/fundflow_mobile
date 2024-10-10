import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // For layout components
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.* // Material3 for UI components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.zIndex
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R // Ensure your image is in res/drawable

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
    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background_top_main),
                    contentDescription = "Background Top Main",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop // Corrected syntax
                )
                Image(
                    painter = painterResource(id = R.drawable.background_cover),
                    contentDescription = "Background Cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 140.dp)

                        .zIndex(1f),
                    contentScale = ContentScale.Crop // Corrected syntax
                )
            }
        }
    ) { padding ->
        // Konten di bawah gambar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tambahkan konten lainnya di sini
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    FundflowTheme {
        ProfileScreen()
    }
}