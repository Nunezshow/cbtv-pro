package com.cbuildz.tvpro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import com.cbuildz.tvpro.ui.TVButtonDefaults

@Composable
fun HomeScreen(
    onAddPlaylist: () -> Unit,
    onPlayTest: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("cBuildz TV Pro")

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onAddPlaylist, colors = TVButtonDefaults.colors()) {
                Text("Add Playlist")
            }
            Button(onClick = onPlayTest, colors = TVButtonDefaults.colors()) {
                Text("Play Test HLS")
            }
            Button(onClick = { onNavigate(Routes.CHANNEL_LIST) }, colors = TVButtonDefaults.colors()) {
                Text("Browse Channels")
            }
            Button(onClick = { onNavigate(Routes.SETTINGS) }, colors = TVButtonDefaults.colors()) {
                Text("Settings")
            }
        }
    }
}
