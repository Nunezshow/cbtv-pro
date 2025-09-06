diff --git a/app/src/main/java/com/cbuildz/tvpro/ui/screens/SettingsScreen.kt b/app/src/main/java/com/cbuildz/tvpro/ui/screens/SettingsScreen.kt
index 613b0423eb49d3fc587fea0ea0dcd82484bfa8a0..0d5d04e6fc17d0b7727b5d82c044d9b39e5b7bea 100644
--- a/app/src/main/java/com/cbuildz/tvpro/ui/screens/SettingsScreen.kt
+++ b/app/src/main/java/com/cbuildz/tvpro/ui/screens/SettingsScreen.kt
@@ -1,44 +1,52 @@
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
+    onClearLastChannel: () -> Unit,
     onBack: () -> Unit
 ) {
     Column(Modifier.fillMaxSize().padding(24.dp)) {
         Text("Settings")
 
         Spacer(Modifier.height(24.dp))
 
         Button(onClick = onToggleRememberLast, colors = TVButtonDefaults.colors()) {
             Text("Remember last channel: ${if (rememberLast) "ON" else "OFF"}")
         }
 
+        if (rememberLast) {
+            Spacer(Modifier.height(24.dp))
+            Button(onClick = onClearLastChannel, colors = TVButtonDefaults.colors()) {
+                Text("Clear saved channel")
+            }
+        }
+
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
