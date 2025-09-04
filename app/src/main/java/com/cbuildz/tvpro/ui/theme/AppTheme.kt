package com.cbuildz.tvpro.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private fun schemeForAccent(accent: String): ColorScheme {
    val (primary, secondary) = when (accent.lowercase()) {
        "red" -> RedPrimary to RedSecondary
        "green" -> GreenPrimary to GreenSecondary
        else -> CyanPrimary to CyanSecondary
    }
    return darkColorScheme(
        primary = primary,
        secondary = secondary,
        background = DarkBg,
        surface = DarkSurface,
        onBackground = OnDark,
        onSurface = OnDark,
        onPrimary = OnDark,
        onSecondary = OnDarkVariant
    )
}

@Composable
fun AppTheme(accent: String, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = schemeForAccent(accent),
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
