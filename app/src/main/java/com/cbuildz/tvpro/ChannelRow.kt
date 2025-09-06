diff --git a//dev/null b/app/src/main/java/com/cbuildz/tvpro/ChannelRow.kt
index 0000000000000000000000000000000000000000..ac04216cab9a2877c6850af761a50e3ef7d55be0 100644
--- a//dev/null
+++ b/app/src/main/java/com/cbuildz/tvpro/ChannelRow.kt
@@ -0,0 +1,67 @@
+package com.cbuildz.tvpro
+
+import androidx.compose.foundation.Image
+import androidx.compose.foundation.layout.*
+import androidx.compose.material3.MaterialTheme
+import androidx.compose.material3.Text
+import androidx.compose.runtime.Composable
+import androidx.compose.ui.Modifier
+import androidx.compose.ui.layout.ContentScale
+import androidx.compose.ui.text.font.FontWeight
+import androidx.compose.ui.unit.dp
+import coil.compose.rememberAsyncImagePainter
+import com.cbuildz.tvpro.ui.TVButtonDefaults
+import androidx.tv.material3.Button
+
+@Composable
+fun ChannelRow(
+    channel: Channel,
+    isFavorite: Boolean,
+    onToggleFavorite: () -> Unit,
+    onClick: () -> Unit
+) {
+    Row(
+        modifier = Modifier
+            .fillMaxWidth()
+            .height(IntrinsicSize.Min)
+            .padding(vertical = 6.dp),
+        horizontalArrangement = Arrangement.SpaceBetween
+    ) {
+        Row(Modifier.weight(1f)) {
+            if (!channel.logo.isNullOrEmpty()) {
+                Image(
+                    painter = rememberAsyncImagePainter(channel.logo),
+                    contentDescription = channel.name,
+                    modifier = Modifier
+                        .size(48.dp)
+                        .padding(end = 12.dp),
+                    contentScale = ContentScale.Fit
+                )
+            }
+            Column {
+                Text(
+                    channel.name,
+                    style = MaterialTheme.typography.bodyLarge,
+                    fontWeight = FontWeight.Medium
+                )
+                channel.group?.let {
+                    Text(
+                        it,
+                        style = MaterialTheme.typography.bodySmall,
+                        color = MaterialTheme.colorScheme.onSurfaceVariant
+                    )
+                }
+            }
+        }
+
+        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
+            Button(onClick = onClick, colors = TVButtonDefaults.colors()) {
+                Text("Play")
+            }
+            Button(onClick = onToggleFavorite, colors = TVButtonDefaults.colors()) {
+                Text(if (isFavorite) "★" else "☆")
+            }
+        }
+    }
+}
+
