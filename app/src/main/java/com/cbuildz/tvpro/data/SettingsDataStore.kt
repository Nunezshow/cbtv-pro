package com.cbuildz.tvpro.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val REMEMBER_LAST = booleanPreferencesKey("remember_last")
        val ACCENT = stringPreferencesKey("accent")
        val FAVORITES = stringSetPreferencesKey("favorites")
        val LAST_CHANNEL = stringPreferencesKey("last_channel")
        val PLAYLISTS = stringSetPreferencesKey("playlist_urls")
    }

    private val ds get() = context.settingsDataStore

    // Remember last channel
    fun shouldRememberLast(): Flow<Boolean> =
        ds.data.map { it[Keys.REMEMBER_LAST] ?: true }

    suspend fun setRememberLast(value: Boolean) {
        ds.edit { it[Keys.REMEMBER_LAST] = value }
    }

    // Accent color: "cyan" (default) | "red" | "green"
    fun getAccent(): Flow<String> =
        ds.data.map { it[Keys.ACCENT] ?: "cyan" }

    suspend fun setAccent(value: String) {
        ds.edit { it[Keys.ACCENT] = value }
    }

    // Favorites (by channel URL)
    fun getFavorites(): Flow<Set<String>> =
        ds.data.map { it[Keys.FAVORITES] ?: emptySet() }

    suspend fun saveFavorites(value: Set<String>) {
        ds.edit { it[Keys.FAVORITES] = value }
    }

    // Last channel URL
    fun getLastChannel(): Flow<String?> =
        ds.data.map { it[Keys.LAST_CHANNEL] }

    suspend fun saveLastChannel(url: String) {
        ds.edit { it[Keys.LAST_CHANNEL] = url }
    }

    // Playlists (URLs)
    fun getPlaylistUrls(): Flow<Set<String>> =
        ds.data.map { it[Keys.PLAYLISTS] ?: emptySet() }

    suspend fun addPlaylistUrl(url: String) {
        ds.edit { prefs ->
            val set = prefs[Keys.PLAYLISTS]?.toMutableSet() ?: mutableSetOf()
            set.add(url)
            prefs[Keys.PLAYLISTS] = set
        }
    }

    suspend fun removePlaylistUrl(url: String) {
        ds.edit { prefs ->
            val set = prefs[Keys.PLAYLISTS]?.toMutableSet() ?: mutableSetOf()
            set.remove(url)
            prefs[Keys.PLAYLISTS] = set
        }
    }
}
