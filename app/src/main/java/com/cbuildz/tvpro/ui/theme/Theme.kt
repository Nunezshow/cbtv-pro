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
    primary = CyanPrimary,
    secondary = CyanSecondary,
    background = DarkBackground,
    onBackground = TextPrimary
)

private val LightColors = lightColorScheme(
    primary = CyanPrimary,
    secondary = CyanSecondary,
    background = LightBackground,
    onBackground = TextSecondary
)

@Composable
fun AppTheme(
    settings: SettingsDataStore,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val accent by settings.getAccent().collectAsState(initial = "cyan")
    val isDark = isSystemInDarkTheme()

    val (primary, secondary) = when (accent) {
        "red" -> RedPrimary to RedSecondary
        "green" -> GreenPrimary to GreenSecondary
        else -> CyanPrimary to CyanSecondary
    }

    val colors = if (isDark) {
        DarkColors.copy(primary = primary, secondary = secondary)
    } else {
        LightColors.copy(primary = primary, secondary = secondary)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
