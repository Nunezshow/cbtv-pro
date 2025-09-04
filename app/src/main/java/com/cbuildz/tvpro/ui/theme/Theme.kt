package com.cbuildz.tvpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

// Accent state - can be updated later from Settings
val currentAccent = mutableStateOf(AccentCyan)

private fun darkColorPalette(accent: Color) = darkColorScheme(
    primary = accent,
    secondary = accent,
    background = DarkBackground,
    surface = LightBackground,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun TVProTheme(content: @Composable () -> Unit) {
    val accent = remember { currentAccent }

    MaterialTheme(
        colorScheme = darkColorPalette(accent.value),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
