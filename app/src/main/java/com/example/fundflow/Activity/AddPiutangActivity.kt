package com.example.fundflow.Activity

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fundflow.Activity.ui.theme.FundflowTheme
import com.example.fundflow.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Locale

class AddPiutangActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)


        setContent {
            FundflowTheme {
                mainstateAddPiutangActivity()
            }
        }
    }
}

@Composable
fun mainstateAddPiutangActivity() {
    val context = LocalContext.current
    val JatuhTempoState = remember { mutableStateOf<Timestamp?>(null) } // Menggunakan Timestamp nullable
    val TanggalAwalState = remember { mutableStateOf<Timestamp?>(null) }
    val keteranganState = remember { mutableStateOf("") }
    val NominalState = remember { mutableStateOf("") }
    val JenisDateState = remember { mutableStateOf("piutang") }
    val UserIdState = remember { mutableStateOf("") }
    var isSelectingJatuhTempo by remember { mutableStateOf(false) }
    var isSelectingTanggalAwal by remember { mutableStateOf(false) }

    val tanggalAwalText = TanggalAwalState.value?.toDate()?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
    } ?: "Pilih Tanggal Awal"

    val jatuhTempoText = JatuhTempoState.value?.toDate()?.let {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
    } ?: "Pilih Jatuh Tempo"

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
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val timestamp = Timestamp(calendar.time)

                // Perbarui state sesuai jenis tanggal yang sedang dipilih
                if (isSelectingTanggalAwal) {
                    TanggalAwalState.value = timestamp
                } else if (isSelectingJatuhTempo) {
                    JatuhTempoState.value = timestamp
                }

                showDatePicker = false // Reset state setelah memilih tanggal
            }, year, month, day
        )

        datePickerDialog.setOnCancelListener {
            showDatePicker = false // Reset state jika dialog dibatalkan
        }

        datePickerDialog.show()
    }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == RESULT_OK && data != null) {
            val selectedMediaUri = data.data
            // Lakukan sesuatu dengan media yang dipilih, misalnya tampilkan di UI atau simpan
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
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
                        .clickable { (context as? ComponentActivity)?.finish() }
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Tambah Daftar Piutang",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(58.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Tanggal Awal:" +
                                " $tanggalAwalText",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.tanggal_btn),
                        contentDescription = "Tanggal Awal",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                isSelectingTanggalAwal = true
                                isSelectingJatuhTempo = false
                                showDatePicker = true
                            }
                    )
                }


                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Jatuh Tempo:" +
                                    "$jatuhTempoText",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.tanggal_btn),
                            contentDescription = "Jatuh Tempo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    isSelectingTanggalAwal = false
                                    isSelectingJatuhTempo = true
                                    showDatePicker = true
                                }
                        )

                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 46.dp, bottom = 46.dp)
                ) {
                    items(listOf("Nominal", "Keterangan")) { label ->
                        val textState = when (label) {
                            "Nominal" -> NominalState
                            "Keterangan" -> keteranganState

                            else -> remember { mutableStateOf("") }
                        }

                        InputFieldWithLabel(label = label, textState = textState)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                }
            }

            Button(
                onClick = {
                    saveUtangPiutangToFirestore(
                        context = context, // Pastikan context diteruskan
                        jatuhTempo = JatuhTempoState.value?: Timestamp.now(), // Menggunakan nilai dari state jatuhTempo
                        jenis = JenisDateState.value, // Menggunakan nilai dari state jenis
                        keterangan = keteranganState.value, // Menggunakan nilai dari state keterangan
                        nominal = NominalState.value.toLongOrNull() ?: 0L, // Mengonversi ke Long
                        tanggalAwal = TanggalAwalState.value?: Timestamp.now(), // Menggunakan nilai dari state tanggalAwal
                        userId = UserIdState.value // Menggunakan nilai dari state userId
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00549C)),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
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

fun saveUtangPiutangToFirestore(
    context: Context,
    jatuhTempo: Timestamp,
    jenis: String,
    keterangan: String,
    nominal: Long,
    tanggalAwal: Timestamp,
    userId: String
) {
    val firestore = FirebaseFirestore.getInstance()


    val utangData = hashMapOf(
        "jatuh tempo" to jatuhTempo,
        "jenis" to jenis,
        "keterangan" to keterangan,
        "nominal" to nominal,
        "tanggal awal" to tanggalAwal,
        "userid" to userId
    )

    firestore.collection("utang piutang").add(utangData)
        .addOnSuccessListener { documentReference ->
            println("Data utang berhasil disimpan dengan ID: ${documentReference.id}")
            // Menampilkan Toast sebagai tanda data berhasil disimpan
            Toast.makeText(
                context, // Pastikan context telah tersedia
                "Data utang berhasil disimpan!",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener { e ->
            println("Gagal menyimpan data utang: $e")
            // Menampilkan Toast jika terjadi kegagalan
            Toast.makeText(
                context, // Pastikan context telah tersedia
                "Gagal menyimpan data utang!",
                Toast.LENGTH_SHORT
            ).show()
        }

}


    @Composable
    fun InputFieldWithLabel2(label: String) {
        val textState = remember { mutableStateOf("") }

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
                    value = textState.value,
                    onValueChange = { textState.value = it },
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
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent, // Nonaktifkan background
                        unfocusedContainerColor = Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun AddPiutangPreview() {
        FundflowTheme {
            mainstateAddPiutangActivity()
        }
    }
