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

                    // Mengurangi spasi untuk menggeser tombol ke atas
                    Spacer(modifier = Modifier.weight(0.02f)) // Menurunkan nilai weight untuk mengangkat tombol

                    // Row untuk tombol Login dan Google Login di halaman terakhir
                    if (pagerState.currentPage == 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically // Pastikan tombol berada di tengah secara vertikal
                        ) {
                            ButtonUi(
                                text = "Login",
                                backgroundColor = ButtonBackgroundColor,
                                textColor = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp) // Ukuran tinggi tombol
                                    .clip(RoundedCornerShape(25.dp)), // Rounded corners
                                onClick = {
                                    onFinished()
                                },
                                fontSize = 16
                            )

                            Spacer(modifier = Modifier.width(16.dp)) // Beri jarak di antara kedua tombol

                            GoogleLoginButton(
                                onClick = {
                                    // Aksi login dengan Google
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp) // Ukuran tinggi tombol
                                    .clip(RoundedCornerShape(25.dp)) // Rounded corners
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
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
                                    .height(50.dp) // Ukuran tinggi tombol
                                    .clip(RoundedCornerShape(25.dp)) // Rounded corners
                            )
                        }
                    }
                }

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
    OnBoardingScreen {}
}
