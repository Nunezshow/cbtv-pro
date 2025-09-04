package com.cbuildz.tvpro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import com.cbuildz.tvpro.ui.TVButtonDefaults

@Composable
fun SettingsScreen(
    rememberLast: Boolean,
    onToggleRememberLast: () -> Unit,
    onAccentSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Settings")

        Spacer(Modifier.height(24.dp))

        Button(onClick = onToggleRememberLast, colors = TVButtonDefaults.colors()) {
            Text("Remember last channel: ${if (rememberLast) "ON" else "OFF"}")
        }

        Spacer(Modifier.height(24.dp))

        Text("Accent color")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("cyan", "red", "green").forEach { color ->
                Button(onClick = { onAccentSelected(color) }, colors = TVButtonDefaults.colors()) {
                    Text(color)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(onClick = onBack, colors = TVButtonDefaults.colors()) {
            Text("Back")
        }
    }
}
