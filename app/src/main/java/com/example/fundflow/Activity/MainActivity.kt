package com.example.fundflow.Activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
        actionBar?.hide()

        setContent {
            FundflowTheme {
                ShowOnBoardingScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowOnBoardingScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
        OnBoardingScreen {
            // Panggil LoginActivity untuk menampilkan bottom sheet
            LoginActivity.showLoginBottomSheet(context)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FundflowTheme {
        ShowOnBoardingScreen()
    }
}
