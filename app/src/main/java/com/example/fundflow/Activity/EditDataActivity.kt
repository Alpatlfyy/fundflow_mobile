package com.example.fundflow.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import com.example.fundflow.UserSingleton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await



class EditDataActivity : ComponentActivity() {
    val currentUid = UserSingleton.getUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FundflowTheme {

                    mainstateEditData()
                }
            }
        }
    }

@Composable
fun mainstateEditData() {
    val currentUid = UserSingleton.getUid()
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var dataLoaded by remember { mutableStateOf(false) }


    // Firestore reference
    val firestore = FirebaseFirestore.getInstance()
    val documentId = currentUid // Ganti dengan ID dokumen Anda
    val documentRef = firestore.collection("users").document(documentId)

    // Ambil data dari Firestore
    LaunchedEffect(Unit) {
        try {
            val snapshot = documentRef.get().await()
            name = snapshot.getString("name") ?: ""
            username = snapshot.getString("username") ?: ""
            email = snapshot.getString("email") ?: ""
            role = snapshot.getString("role") ?: ""
            telepon = snapshot.getString("telepon") ?: ""
            dataLoaded = true
        } catch (e: Exception) {
            Log.e("Firestore Load", "Gagal memuat data", e)
            Toast.makeText(context, "Gagal memuat data: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row
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
                        .clickable { (context as? ComponentActivity)?.onBackPressed() }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Edit data pribadi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(36.dp))

            // LazyColumn for Input Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                if (dataLoaded) {

                    InputFieldWithLabel5("Nama", name) { name = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    InputFieldWithLabel5("Username", username) { username = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    InputFieldWithLabel5("Email", email) { email = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    InputFieldWithLabel5("Role", role) { role = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    InputFieldWithLabel5("Telepon", telepon) { telepon = it }
                    Spacer(modifier = Modifier.height(80.dp))


                    // Save Button
                    Button(
                        onClick = {
                            // Validasi data sebelum menyimpan
                            if (name.isBlank() || username.isBlank() || email.isBlank() || role == "Pilih Jabatan" || telepon.isBlank()) {
                                Toast.makeText(context, "Harap isi semua data dengan benar", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }

                            val firestore = FirebaseFirestore.getInstance()
                            val document = firestore.collection("users").document("ys6Y5FgVE90mKZ0W9NZ5")

                            // Data yang akan disimpan
                            val dataToUpdate = mapOf(
                                "name" to name,
                                "username" to username,
                                "email" to email,
                                "role" to role,
                                "telepon" to telepon
                            )

                            // Menyimpan data ke Firestore
                            document.set(
                                dataToUpdate,
                                SetOptions.merge()
                            ) // Menggunakan SetOptions.merge() agar tidak menimpa field lain
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT)
                                        .show()
                                    Log.d("Firestore Update", "Data berhasil diperbarui: $dataToUpdate")
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Gagal memperbarui data: ${e.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e("Firestore Update", "Gagal memperbarui data", e)
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00549C)),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Simpan",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun InputFieldWithLabel5(label: String, value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)) // Menjaga rounded corners
                    .height(50.dp)
                    .background(Color.White) // Latar belakang putih
                    .border(
                        BorderStroke(
                            1.dp,
                            Color.Gray
                        ), // Menambahkan stroke dengan ketebalan 1dp dan warna abu-abu
                        shape = RoundedCornerShape(8.dp) // Bentuk rounded corners sesuai background
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent, // Nonaktifkan background
                    textColor = Color.Black
                )
            )
        }
    }
}



@Composable
fun ComboBoxJabatan(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    roleOptions: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedRole,
            onValueChange = {},
            label = { Text("Jabatan") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Icon",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(start = 16.dp, end=16.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            roleOptions.forEach { option ->
                DropdownMenuItem(
                    content = { Text(option) },
                    onClick = {
                        onRoleSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    FundflowTheme {
        mainstateEditData()
    }
}