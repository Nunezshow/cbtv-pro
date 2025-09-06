diff --git a/app/src/main/java/com/cbuildz/tvpro/data/SettingsDataStore.kt b/app/src/main/java/com/cbuildz/tvpro/data/SettingsDataStore.kt
index 64f86b59279cbbec57b6171bea567e6a99a848b6..d28c7b52138f4f51b87696dc127161bcb736a192 100644
--- a/app/src/main/java/com/cbuildz/tvpro/data/SettingsDataStore.kt
+++ b/app/src/main/java/com/cbuildz/tvpro/data/SettingsDataStore.kt
@@ -28,40 +28,44 @@ class SettingsDataStore(private val context: Context) {
 
     // Remember last channel
     fun shouldRememberLast(): Flow<Boolean> =
         context.dataStore.data.map { it[KEY_REMEMBER_LAST] ?: true }
 
     suspend fun setRememberLast(value: Boolean) {
         context.dataStore.edit { it[KEY_REMEMBER_LAST] = value }
     }
 
     // Favorites (channel URLs)
     fun getFavorites(): Flow<Set<String>> =
         context.dataStore.data.map { it[KEY_FAVORITES] ?: emptySet() }
 
     suspend fun saveFavorites(urls: Set<String>) {
         context.dataStore.edit { it[KEY_FAVORITES] = urls }
     }
 
     // Last channel
     fun getLastChannel(): Flow<String?> =
         context.dataStore.data.map { it[KEY_LAST_CHANNEL] }
 
     suspend fun saveLastChannel(url: String) {
         context.dataStore.edit { it[KEY_LAST_CHANNEL] = url }
     }
 
+    suspend fun clearLastChannel() {
+        context.dataStore.edit { it.remove(KEY_LAST_CHANNEL) }
+    }
+
     // Playlists
     fun getPlaylists(): Flow<Set<String>> =
         context.dataStore.data.map { it[KEY_PLAYLISTS] ?: emptySet() }
 
     suspend fun savePlaylists(urls: Set<String>) {
         context.dataStore.edit { it[KEY_PLAYLISTS] = urls }
     }
 
     suspend fun addPlaylist(url: String) {
         context.dataStore.edit { prefs ->
             val current = prefs[KEY_PLAYLISTS] ?: emptySet()
             prefs[KEY_PLAYLISTS] = current + url
         }
     }
 }
