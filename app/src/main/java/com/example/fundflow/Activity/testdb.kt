package com.example.fundflow.Activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    val alamat: String? = null,
    val email: String? = null,
    val jabatan: String? = null,
    val perusahaan: String? = null,
    val telepon: Long? = null, // Tetap sebagai Long
    val username: String? = null,
    val password: String? = null // Tambahkan field password
)


class testdb : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FundflowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UserListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun UserListScreen(modifier: Modifier = Modifier) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        getUserData(
            onSuccess = { users = it },
            onError = { errorMessage = it.message }
        )
    }

    if (errorMessage != null) {
        Text(text = "Error: $errorMessage", modifier = modifier)
    } else {
        LazyColumn(modifier = modifier) {
            items(users) { user ->
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User) {
    Text(text = "Username: ${user.username ?: "N/A"}")
    Text(text = "Alamat: ${user.alamat ?: "N/A"}")
    Text(text = "Email: ${user.email ?: "N/A"}")
    Text(text = "Jabatan: ${user.jabatan ?: "N/A"}")
    Text(text = "Perusahaan: ${user.perusahaan ?: "N/A"}")
    Text(text = "Telepon: ${user.telepon ?: "N/A"}")
    Text(text = "------------------")
}

fun getUserData(onSuccess: (List<User>) -> Unit, onError: (Exception) -> Unit) {
    val db = Firebase.firestore
    db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            val users = result.mapNotNull { document ->
                document.toObject(User::class.java)
            }
            onSuccess(users)
        }
        .addOnFailureListener { exception ->
            onError(exception)
        }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    FundflowTheme {
        UserListScreen()
    }
}
