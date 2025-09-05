package com.cbuildz.tvpro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cbuildz.tvpro.data.SettingsDataStore

@Composable
fun FavoritesScreen(
    channels: List<Channel>,
    favorites: Set<String>,
    onChannelClick: (Channel) -> Unit,
    onToggleFavorite: (Channel) -> Unit
) {
    val favoriteChannels = channels.filter { favorites.contains(it.url) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (favoriteChannels.isEmpty()) {
            Text("No favorites yet")
        } else {
            LazyColumn {
                items(favoriteChannels) { channel ->
                    ChannelRow(
                        channel = channel,
                        isFavorite = favorites.contains(channel.url),
                        onClick = { onChannelClick(channel) },
                        onToggleFavorite = { onToggleFavorite(channel) }
                    )
                }
            }
        }
    }
}
