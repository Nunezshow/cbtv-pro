package com.cbuildz.tvpro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.cbuildz.tvpro.data.SettingsDataStore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val settings = SettingsDataStore(LocalContext.current)
    val accent by settings.getAccentColor().collectAsState(initial = "cyan")

    val color = when (accent) {
        "red" -> Color.Red
        "green" -> Color.Green
        else -> Color.Cyan
    }

    val colors = lightColorScheme(
        primary = color,
        secondary = color,
        tertiary = color
    )

    MaterialTheme(colorScheme = colors, content = content)
}
