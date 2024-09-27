    package com.example.fundflow.ui.theme

    import android.app.Activity
    import android.os.Build
    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.darkColorScheme
    import androidx.compose.material3.dynamicDarkColorScheme
    import androidx.compose.material3.dynamicLightColorScheme
    import androidx.compose.material3.lightColorScheme
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.toArgb
    import androidx.compose.ui.platform.LocalContext
    import androidx.core.view.WindowCompat

    private val DarkColorScheme = darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80
    )

    private val LightColorScheme = lightColorScheme(
//        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40
    )

    @Composable
    fun FundflowTheme(
        darkTheme: Boolean = false, // Paksa untuk selalu menggunakan mode light
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
    ) {
        val context = LocalContext.current
        val activity = context as Activity

        // Tentukan color scheme
        val colorScheme = when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                dynamicLightColorScheme(context) // Selalu gunakan dynamic light color scheme
            }
            else -> LightColorScheme
        }

        // Update warna status bar
        activity.window.statusBarColor = Color.White.toArgb() // Selalu putih untuk mode light

        // Kontrol tampilan ikon di status bar
        val windowInsetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        windowInsetsController?.isAppearanceLightStatusBars = true // Ikon status bar tetap gelap di mode light

        // Terapkan MaterialTheme
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            content = content
        )
    }
