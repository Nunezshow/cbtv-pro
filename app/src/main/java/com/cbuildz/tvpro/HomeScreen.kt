package com.cbuildz.tvpro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.tv.material3.Button
import com.cbuildz.tvpro.ui.TVButtonDefaults

@Composable
fun HomeScreen(
    nav: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("cBuildz TV Pro")

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { nav.navigate(Routes.ADD_PLAYLIST) },
                colors = TVButtonDefaults.colors()
            ) {
                Text("Add Playlist")
            }

            Button(
                onClick = { nav.navigate("${Routes.PLAYER}/https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8") },
                colors = TVButtonDefaults.colors()
            ) {
                Text("Play Test HLS")
            }

            Button(
                onClick = { nav.navigate(Routes.SETTINGS) },
                colors = TVButtonDefaults.colors()
            ) {
                Text("Settings")
            }

            Button(
                onClick = { nav.navigate(Routes.CHANNEL_LIST) },
                colors = TVButtonDefaults.colors()
            ) {
                Text("Browse Channels")
            }
        }
    }
}
