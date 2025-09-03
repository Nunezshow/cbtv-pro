package com.cbuildz.tvpro

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button

@Composable
fun AddPlaylistScreen(
    onPlaylistAdded: (String) -> Unit,
    onBack: () -> Unit
) {
    var url by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp)
    ) {
        Text("Enter M3U Playlist URL:", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Playlist URL") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { if (url.isNotBlank()) onPlaylistAdded(url) }) { Text("Save") }
            Button(onClick = onBack) { Text("Cancel") }
        }
    }
}
