package com.cbuildz.tvpro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TVProTheme(
    accent: String,
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val (primary, secondary) = when (accent.lowercase()) {
        "red" -> RedPrimary to RedSecondary
        "green" -> GreenPrimary to GreenSecondary
        else -> CyanPrimary to CyanSecondary
    }

    val scheme = if (darkTheme) {
        darkColorScheme(
            primary = primary,
            onPrimary = Color.White,
            secondary = secondary,
            onSecondary = Color.White,
            background = DarkBackground,
            onBackground = TextPrimary,
            surface = DarkBackground,
            onSurface = TextPrimary
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            secondary = secondary,
            onSecondary = Color.White,
            background = LightBackground,
            onBackground = TextOnLight,
            surface = LightBackground,
            onSurface = TextOnLight
        )
    }

    MaterialTheme(
        colorScheme = scheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
