package com.example.fundflow.Activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.fundflow.onboarding.OnBoardingScreen
import com.example.fundflow.ui.theme.FundflowTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        actionBar?.hide()

        // Ambil parameter initialPage dari intent
        val initialPage = intent.getIntExtra("INITIAL_PAGE", 0)

        setContent {
            FundflowTheme {
                ShowOnBoardingScreen(initialPage)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowOnBoardingScreen(initialPage: Int) {
    val context = LocalContext.current

    Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
        OnBoardingScreen(onFinished = {
            // Panggil LoginActivity untuk menampilkan bottom sheet
            LoginActivity.showLoginBottomSheet(context)
        }, initialPage = initialPage) // Kirimkan initialPage ke OnBoardingScreen
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FundflowTheme {
        ShowOnBoardingScreen(0) // Menampilkan halaman pertama sebagai contoh
    }
}
