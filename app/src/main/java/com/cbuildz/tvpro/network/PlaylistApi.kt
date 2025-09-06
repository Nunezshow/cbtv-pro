diff --git a//dev/null b/app/src/main/java/com/cbuildz/tvpro/network/PlaylistApi.kt
index 0000000000000000000000000000000000000000..3959267064ed12bb86808098f84f79311fe781a8 100644
--- a//dev/null
+++ b/app/src/main/java/com/cbuildz/tvpro/network/PlaylistApi.kt
@@ -0,0 +1,39 @@
+package com.cbuildz.tvpro.network
+
+import kotlinx.coroutines.Dispatchers
+import kotlinx.coroutines.withContext
+import org.json.JSONArray
+import java.net.HttpURLConnection
+import java.net.URL
+
+data class PlaylistInfo(
+    val id: String,
+    val name: String,
+    val url: String,
+    val isGlobal: Boolean,
+    val userId: String?
+)
+
+/**
+ * Simple helper for fetching playlists from the backend. Returns both global
+ * and user-owned playlists in a single list.
+ */
+object PlaylistApi {
+    suspend fun fetch(baseUrl: String, token: String): List<PlaylistInfo> = withContext(Dispatchers.IO) {
+        val conn = URL("$baseUrl/playlists").openConnection() as HttpURLConnection
+        conn.setRequestProperty("Authorization", "Bearer $token")
+        conn.connect()
+        val body = conn.inputStream.bufferedReader().use { it.readText() }
+        val arr = JSONArray(body)
+        List(arr.length()) { index ->
+            val obj = arr.getJSONObject(index)
+            PlaylistInfo(
+                id = obj.getString("id"),
+                name = obj.getString("name"),
+                url = obj.getString("url"),
+                isGlobal = obj.getBoolean("isGlobal"),
+                userId = if (obj.isNull("userId")) null else obj.getString("userId")
+            )
+        }
+    }
+}
