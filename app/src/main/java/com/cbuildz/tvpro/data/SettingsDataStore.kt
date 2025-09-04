package com.cbuildz.tvpro.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("tvpro_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val FAVORITES = stringSetPreferencesKey("favorites")
        private val PLAYLIST_URL = stringPreferencesKey("playlist_url")
        private val ACCENT_COLOR = stringPreferencesKey("accent_color")
    }

    fun getFavorites(): Flow<Set<String>> =
        context.dataStore.data.map { it[FAVORITES] ?: emptySet() }

    suspend fun saveFavorites(favs: Set<String>) {
        context.dataStore.edit { prefs -> prefs[FAVORITES] = favs }
    }

    fun getPlaylistUrl(): Flow<String?> =
        context.dataStore.data.map { it[PLAYLIST_URL] }

    suspend fun savePlaylistUrl(url: String) {
        context.dataStore.edit { prefs -> prefs[PLAYLIST_URL] = url }
    }

    fun getAccentColor(): Flow<String> =
        context.dataStore.data.map { it[ACCENT_COLOR] ?: "cyan" }

    suspend fun saveAccentColor(color: String) {
        context.dataStore.edit { prefs -> prefs[ACCENT_COLOR] = color }
    }
}
