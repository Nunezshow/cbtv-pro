package com.cbuildz.tvpro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.cbuildz.tvpro.ui.TVButtonDefaults
import androidx.tv.material3.Button

@Composable
fun ChannelListScreen(
    channels: List<Channel>,
    favorites: Set<String>,   // favorites by URL
    onToggleFavorite: (Channel) -> Unit,
    onChannelSelected: (Channel) -> Unit,
    onBack: () -> Unit,
    onFavorites: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Channels",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(channels) { channel ->
                ChannelRow(
                    channel = channel,
                    isFavorite = favorites.contains(channel.url),
                    onToggleFavorite = { onToggleFavorite(channel) },
                    onClick = { onChannelSelected(channel) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onBack, colors = TVButtonDefaults.colors()) {
                Text("Back")
            }
            if (favorites.isNotEmpty()) {
                Button(onClick = onFavorites, colors = TVButtonDefaults.colors()) {
                    Text("Favorites")
                }
            }
        }
    }
}

@Composable
fun ChannelRow(
    channel: Channel,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.weight(1f)) {
            if (!channel.logo.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(channel.logo),
                    contentDescription = channel.name,
                    modifier = Modifier.size(48.dp).padding(end = 12.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Column {
                Text(
                    channel.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                channel.group?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onClick, colors = TVButtonDefaults.colors()) {
                Text("Play")
            }
            Button(onClick = onToggleFavorite, colors = TVButtonDefaults.colors()) {
                Text(if (isFavorite) "★" else "☆")
            }
        }
    }
}
