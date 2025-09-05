package com.cbuildz.tvpro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TVProTheme(
    accentName: String = "cyan",
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val accent = when (accentName.lowercase()) {
        "red"   -> AccentRed
        "green" -> AccentGreen
        else    -> AccentCyan
    }

    val dark = darkColorScheme(
        primary = accent,
        onPrimary = Color.Black,
        secondary = accent,
        background = DarkBackground,
        surface = DarkBackground,
        onBackground = TextOnDark,
        onSurface = TextOnDark
    )

    val light = lightColorScheme(
        primary = accent,
        onPrimary = Color.White,
        secondary = accent,
        background = LightBackground,
        surface = LightBackground,
        onBackground = TextOnLight,
        onSurface = TextOnLight
    )

    MaterialTheme(
        colorScheme = if (darkTheme) dark else light,
        typography = androidx.compose.material3.Typography(),
        shapes = androidx.compose.material3.Shapes(),
        content = content
    )
}
