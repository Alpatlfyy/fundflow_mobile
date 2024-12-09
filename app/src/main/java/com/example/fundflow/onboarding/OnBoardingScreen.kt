package com.example.fundflow.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

val ButtonBackgroundColor = Color(android.graphics.Color.parseColor("#0F78CB"))

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnBoardingScreen(onFinished: () -> Unit, initialPage: Int = 0) {
    val pages = listOf(
        OnBoardingModel.FirstPages,
        OnBoardingModel.SecondPages,
        OnBoardingModel.ThirdPages
    )

    // Menggunakan initialPage untuk mengatur halaman awal
    val pagerState = rememberPagerState(initialPage = initialPage) {
        pages.size
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(6f)
                    ) { index ->
                        OnBoardingGraphUI(onBoardingModel = pages[index])
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    IndicatorUI(
                        pageSize = pages.size,
                        currentPage = pagerState.currentPage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 0.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Kolom untuk tombol "Login" pada slide ketiga
                    if (pagerState.currentPage == 2) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ButtonUi(
                                text = "Login",
                                backgroundColor = ButtonBackgroundColor,
                                textColor = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(25.dp)),
                                onClick = {
                                    onFinished()
                                },
                                fontSize = 16
                            )
                        }
                    } else {
                        // Tombol "Lanjut" sebelum halaman terakhir
                        ButtonUi(
                            text = "Lanjut",
                            backgroundColor = ButtonBackgroundColor,
                            textColor = Color.White,
                            onClick = {
                                scope.launch {
                                    if (pagerState.currentPage < pages.size - 1) {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                }
                            },
                            fontSize = 16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }

                // Tombol Skip untuk langsung ke halaman terakhir
                IconButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pages.size - 1)
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
                        fontWeight = FontWeight.Medium
                    )
                }

                // Tombol Back untuk kembali ke halaman sebelumnya
                if (pagerState.currentPage > 0) {
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
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnBoardingScreen(onFinished = {}, initialPage = 0)
}
