package com.example.fundflow.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val ButtonBackgroundColor = Color(0xFF3F40FC)

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun OnBoardingScreen(onFinished: () -> Unit) {

    val pages = listOf(
        OnBoardingModel.FirstPages,
        OnBoardingModel.SecondPages,
        OnBoardingModel.ThirdPages
    )

    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }

    val buttonState = remember {
        derivedStateOf {
            when (pagerState.currentPage) {
                0 -> listOf("", "Lanjut")
                1 -> listOf("", "Lanjut")
                2 -> listOf("", "Mulai")
                else -> listOf("", "")
            }
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Content pager (mengisi 60% dari layar)
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f) // Menggunakan 60% dari tinggi layar
                    ) { index ->
                        OnBoardingGraphUI(onBoardingModel = pages[index])
                    }

                    // Spacer untuk memberikan jarak lebih kecil antara teks dan indikator
                    Spacer(modifier = Modifier.height(2.dp)) // Kurangi tinggi spacer

                    // IndicatorUI placed between pager and button
                    IndicatorUI(
                        pageSize = pages.size,
                        currentPage = pagerState.currentPage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp) // Kurangi padding vertikal
                    )

                    // Spacer to push Button to the bottom
                    Spacer(modifier = Modifier.weight(0.2f)) // Mengisi ruang kosong di atas tombol

                    // Centered ButtonUi at the bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp), // Padding untuk button
                        contentAlignment = Alignment.Center
                    ) {
                        ButtonUi(
                            text = buttonState.value[1],
                            backgroundColor = ButtonBackgroundColor,
                            textColor = Color.White
                        ) {
                            scope.launch {
                                if (pagerState.currentPage < pages.size - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {
                                    onFinished()
                                }
                            }
                        }
                    }
                }

                // Skip Button
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pages.size - 1) // Langsung ke halaman terakhir
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Skip",
                        color = ButtonBackgroundColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium, // Menggunakan Poppins Medium
                        fontFamily = poppinsMedium // Menggunakan font Poppins Medium
                    )
                }

                // Back Button
                IconButton(
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage > 0) {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = ButtonBackgroundColor
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnBoardingScreen {}
}

















