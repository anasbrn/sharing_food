package com.example.sharing_food.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Orange40,
    onPrimary = Color.White,
    primaryContainer = Orange80,
    onPrimaryContainer = Color.Black,
    secondary = OrangeGrey40,
    onSecondary = Color.White,
    background = Color(0xFFFFF3E0),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Orange80,
    onPrimary = Color.Black,
    primaryContainer = OrangeAccent80,
    onPrimaryContainer = Color.Black,
    secondary = OrangeGrey80,
    onSecondary = Color.Black,
    background = Color(0xFF3E2723),
    onBackground = Color.White,
    surface = Color(0xFF4E342E),
    onSurface = Color.White
)


@Composable
fun SharingFoodTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}