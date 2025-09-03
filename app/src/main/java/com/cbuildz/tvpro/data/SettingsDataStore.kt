package com.cbuildz.tvpro.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        val FAVORITES = stringSetPreferencesKey("favorites")
        val LAST_CHANNEL = stringPreferencesKey("last_channel")
        val REMEMBER_LAST_CHANNEL = booleanPreferencesKey("remember_last_channel")
        val THEME_ACCENT = stringPreferencesKey("theme_accent")
    }

    // Favorites
    fun getFavorites(): Flow<Set<String>> =
        context.dataStore.data.map { prefs -> prefs[FAVORITES] ?: emptySet() }

    suspend fun saveFavorites(favs: Set<String>) {
        context.dataStore.edit { prefs -> prefs[FAVORITES] = favs }
    }

    // Last channel
    fun getLastChannel(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[LAST_CHANNEL] }

    suspend fun saveLastChannel(url: String) {
        context.dataStore.edit { prefs -> prefs[LAST_CHANNEL] = url }
    }

    // Remember last channel toggle
    fun shouldRememberLast(): Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[REMEMBER_LAST_CHANNEL] ?: true }

    suspend fun setRememberLast(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[REMEMBER_LAST_CHANNEL] = value }
    }

    // Theme accent
    fun getAccent(): Flow<String?> =
        context.dataStore.data.map { prefs -> prefs[THEME_ACCENT] }

    suspend fun setAccent(value: String) {
        context.dataStore.edit { prefs -> prefs[THEME_ACCENT] = value }
    }
}
