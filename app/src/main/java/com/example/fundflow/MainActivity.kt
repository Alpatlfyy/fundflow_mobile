package com.example.fundflow

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fundflow.onboarding.OnBoardingScreen
import com.example.fundflow.ui.theme.FundflowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            FundflowTheme {

                ShowOnBoardingScreen()
            }
        }

    }

}
@Composable
private fun ShowOnBoardingScreen() {
    val context = LocalContext.current

    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)){
        OnBoardingScreen {
            Toast.makeText(context, "OnBoarding Completed", Toast.LENGTH_SHORT).show()
        }
    }


}

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {

}