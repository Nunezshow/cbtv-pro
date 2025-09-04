package com.cbuildz.tvpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cbuildz.tvpro.data.SettingsDataStore
import androidx.compose.ui.platform.LocalContext

private val DarkColors = darkColorScheme(
    primary = AccentCyan,
    secondary = TextSecondary,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val LightColors = lightColorScheme(
    primary = AccentCyan,
    secondary = TextSecondary,
    background = LightBackground,
    surface = LightBackground,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val settings = SettingsDataStore(context)

    val accent by settings.getAccent().collectAsState(initial = "cyan")

    val accentColor = when (accent) {
        "red" -> AccentRed
        "green" -> AccentGreen
        else -> AccentCyan
    }

    val darkColors = DarkColors.copy(primary = accentColor)
    val lightColors = LightColors.copy(primary = accentColor)

    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
