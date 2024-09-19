package com.example.fundflow.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ButtonUi(
    text: String = "Next",
    backgroundColor: Color = Color(0xFF3F40FC),
    textColor: Color = Color.White, // Changed to white for better contrast
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontSize: Int = 14,
    onClick: () -> Unit
) {
    // Define the shape with rounded corners
    val buttonShape = RoundedCornerShape(100.dp)

    // Define the shadow elevation
    val shadowElevation = 4.dp

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = buttonShape,
        modifier = Modifier
            .width(170.dp)  // Set the width of the button
            .height(50.dp)  // Set the height of the button
            .shadow(elevation = shadowElevation, shape = buttonShape) // Add shadow effect
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp, // Use fontSize here if not using textStyle
            style = textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonUiPreview() {
    ButtonUi(
        text = "Start",
        backgroundColor = Color(0xFF3F40FC),
        textColor = Color.White,
        fontSize = 16, // Adjust as needed
        onClick = {}
    )
}




