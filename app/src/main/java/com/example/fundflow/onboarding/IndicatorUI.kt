package com.example.fundflow.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun IndicatorUI(
    pageSize: Int,
    currentPage: Int,
    selectedColor: Color = Color(0xFF3F40FC),
    unselectedColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    indicatorHeight: Dp = 10.dp,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        repeat(pageSize) { index ->
            Spacer(modifier = Modifier.size(2.5.dp))

            Box(
                modifier = Modifier
                    .height(indicatorHeight)
                    .width(if (index == currentPage) 26.dp else 10.dp) // Change width for selected indicator
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = if (index == currentPage) selectedColor else unselectedColor)
            )

            Spacer(modifier = Modifier.size(2.5.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewIndicatorUI() {
    var currentPage by rememberSaveable { mutableStateOf(1) }
    IndicatorUI(
        pageSize = 5,
        currentPage = currentPage,
        selectedColor = Color.Blue,
        unselectedColor = Color.Gray,
        indicatorHeight = 14.dp
    )
}



