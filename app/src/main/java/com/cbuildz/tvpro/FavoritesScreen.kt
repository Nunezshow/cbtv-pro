package com.cbuildz.tvpro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesScreen(
    channels: List<Channel>,
    onChannelClick: (Channel) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (channels.isEmpty()) {
            Text("No favorites yet")
        } else {
            LazyColumn {
                items(channels) { channel ->
                    ChannelRow(
                        channel = channel,
                        isFavorite = true,
                        onClick = { onChannelClick(channel) },
                        onToggleFavorite = { /* handled from list screen */ }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        androidx.tv.material3.Button(onClick = onBack, colors = com.cbuildz.tvpro.ui.TVButtonDefaults.colors()) {
            Text("Back")
        }
    }
}
