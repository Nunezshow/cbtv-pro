package com.cbuildz.tvpro.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    private companion object {
        val KEY_ACCENT = stringPreferencesKey("accent")                 // "cyan", "red", "green"
        val KEY_REMEMBER_LAST = booleanPreferencesKey("remember_last")  // default true
        val KEY_FAVORITES = stringSetPreferencesKey("favorites")        // Set<String> of channel URLs
        val KEY_LAST_CHANNEL = stringPreferencesKey("last_channel")     // String?
        val KEY_PLAYLISTS = stringSetPreferencesKey("playlists")        // Set<String> of playlist URLs
    }

    // Accent
    fun getAccent(): Flow<String> =
        context.dataStore.data.map { it[KEY_ACCENT] ?: "cyan" }

    suspend fun setAccent(value: String) {
        context.dataStore.edit { it[KEY_ACCENT] = value }
    }

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
