package com.cbuildz.tvpro.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val ACCENT = stringPreferencesKey("accent")                    // "cyan" | "red" | "green"
        val REMEMBER_LAST = booleanPreferencesKey("remember_last")     // default true
        val FAVORITES = stringSetPreferencesKey("favorites")           // set of channel URLs
        val LAST_CHANNEL = stringPreferencesKey("last_channel")        // url
    }

    fun getAccent(): Flow<String> =
        context.dataStore.data.map { it[Keys.ACCENT] ?: "cyan" }

    suspend fun setAccent(value: String) {
        context.dataStore.edit { it[Keys.ACCENT] = value.lowercase() }
    }

    fun shouldRememberLast(): Flow<Boolean> =
        context.dataStore.data.map { it[Keys.REMEMBER_LAST] ?: true }

    suspend fun setRememberLast(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REMEMBER_LAST] = enabled }
    }

    fun getFavorites(): Flow<Set<String>> =
        context.dataStore.data.map { it[Keys.FAVORITES] ?: emptySet() }

    suspend fun saveFavorites(urls: Set<String>) {
        context.dataStore.edit { it[Keys.FAVORITES] = urls }
    }

    fun getLastChannel(): Flow<String?> =
        context.dataStore.data.map { it[Keys.LAST_CHANNEL] }

    suspend fun saveLastChannel(url: String) {
        context.dataStore.edit { it[Keys.LAST_CHANNEL] = url }
    }
}
