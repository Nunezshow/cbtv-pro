package com.cbuildz.tvpro.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Accent,
    onPrimary = OnAccent,
    secondary = AccentVariant,
    onSecondary = OnAccent,
    background = Bg,
    onBackground = OnBg,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    primaryContainer = AccentVariant,
    onPrimaryContainer = OnAccent
)

@Composable
fun AppTheme(
    colorScheme: ColorScheme = DarkColors,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
