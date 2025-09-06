diff --git a/app/src/main/java/com/cbuildz/tvpro/PlaylistSource.kt b/app/src/main/java/com/cbuildz/tvpro/PlaylistSource.kt
index d251b1e21c77c6e7a2d0a23353b06844071f6557..f29b6b210cff0d62eecc2b2f6b2c2b40fd1fe474 100644
--- a/app/src/main/java/com/cbuildz/tvpro/PlaylistSource.kt
+++ b/app/src/main/java/com/cbuildz/tvpro/PlaylistSource.kt
@@ -1,51 +1,78 @@
 package com.cbuildz.tvpro
 
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.withContext
 import java.net.URL
 
 object PlaylistSource {
 
     suspend fun loadFromUrl(url: String): List<Channel> = withContext(Dispatchers.IO) {
         try {
             val content = URL(url).readText()
             parseM3U(content)
         } catch (e: Exception) {
             e.printStackTrace()
             emptyList()
         }
     }
 
-    private fun parseM3U(content: String): List<Channel> {
+    internal fun parseM3U(content: String): List<Channel> {
         val lines = content.lines()
         val channels = mutableListOf<Channel>()
         var currentName = ""
         var currentLogo: String? = null
         var currentGroup: String? = null
+        var currentTvgId: String? = null
+        var currentTvgName: String? = null
+        var currentTvgChno: String? = null
 
-        for (line in lines) {
+        for (raw in lines) {
+            val line = raw.trim()
+            if (line.isEmpty() || line == "#EXTM3U") continue
             when {
                 line.startsWith("#EXTINF") -> {
-                    val nameRegex = Regex(",(.+)")
+                    val nameRegex = Regex(",\s*(.+)")
                     val logoRegex = Regex("tvg-logo=\"([^\"]+)\"")
                     val groupRegex = Regex("group-title=\"([^\"]+)\"")
+                    val idRegex = Regex("tvg-id=\"([^\"]+)\"")
+                    val tvgNameRegex = Regex("tvg-name=\"([^\"]+)\"")
+                    val chnoRegex = Regex("tvg-chno=\"([^\"]+)\"")
 
                     currentName = nameRegex.find(line)?.groupValues?.get(1)?.trim() ?: "Unknown"
                     currentLogo = logoRegex.find(line)?.groupValues?.get(1)
                     currentGroup = groupRegex.find(line)?.groupValues?.get(1)
+                    currentTvgId = idRegex.find(line)?.groupValues?.get(1)
+                    currentTvgName = tvgNameRegex.find(line)?.groupValues?.get(1)
+                    currentTvgChno = chnoRegex.find(line)?.groupValues?.get(1)
                 }
-                line.startsWith("http") -> {
-                    channels.add(
-                        Channel(
-                            name = currentName,
-                            url = line.trim(),
-                            logo = currentLogo,
-                            group = currentGroup
+                line.startsWith("#") -> {
+                    // Ignore other tags
+                }
+                else -> {
+                    val valid = try {
+                        val uri = java.net.URI(line)
+                        uri.isAbsolute && uri.scheme != null
+                    } catch (e: Exception) {
+                        false
+                    }
+                    if (valid) {
+                        channels.add(
+                            Channel(
+                                name = currentName,
+                                url = line,
+                                logo = currentLogo,
+                                group = currentGroup,
+                                tvgId = currentTvgId,
+                                tvgName = currentTvgName,
+                                tvgChno = currentTvgChno
+                            )
                         )
-                    )
+                    } else {
+                        println("Skipping invalid URL: $line")
+                    }
                 }
             }
         }
         return channels
     }
 }
