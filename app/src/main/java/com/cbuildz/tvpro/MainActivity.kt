package com.cbuildz.tvpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cbuildz.tvpro.data.SettingsDataStore
import com.cbuildz.tvpro.ui.screens.SettingsScreen
import com.cbuildz.tvpro.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsDataStore(this)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }

                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME
                    ) {
                        composable(Routes.HOME) {
                            HomeScreen(
                                onAddPlaylist = { navController.navigate(Routes.ADD_PLAYLIST) },
                                onPlayTest = {
                                    val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
                                    val encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${Routes.PLAYER}/$encoded")
                                },
                                onBrowseChannels = { navController.navigate(Routes.CHANNEL_LIST) },
                                onSettings = { navController.navigate(Routes.SETTINGS) }
                            )
                        }

                        composable(Routes.CHANNEL_LIST) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = runBlocking { settings.getFavorites().first() },
                                onChannelSelected = { channel ->
                                    val encoded = URLEncoder.encode(channel.url, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${Routes.PLAYER}/$encoded")
                                },
                                onToggleFavorite = { channel ->
                                    val favs = runBlocking { settings.getFavorites().first() }
                                    val updated =
                                        if (favs.contains(channel.url)) favs - channel.url else favs + channel.url
                                    runBlocking { settings.saveFavorites(updated) }
                                },
                                onBack = { navController.popBackStack() },
                                onFavorites = { navController.navigate(Routes.FAVORITES) }
                            )
                        }

                        composable(
                            route = "${Routes.PLAYER}/{url}",
                            arguments = listOf(navArgument("url") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url")
                                ?.let { java.net.URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
                                ?: return@composable
                            PlayerScreen(url = url)
                        }

                        composable(Routes.FAVORITES) {
                            FavoritesScreen(
                                channels = channels,
                                favorites = runBlocking { settings.getFavorites().first() },
                                onChannelClick = { channel ->
                                    val encoded = URLEncoder.encode(channel.url, StandardCharsets.UTF_8.toString())
                                    navController.navigate("${Routes.PLAYER}/$encoded")
                                },
                                onToggleFavorite = { channel ->
                                    val favs = runBlocking { settings.getFavorites().first() }
                                    val updated =
                                        if (favs.contains(channel.url)) favs - channel.url else favs + channel.url
                                    runBlocking { settings.saveFavorites(updated) }
                                }
                            )
                        }

                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                rememberLast = runBlocking { settings.shouldRememberLast().first() },
                                onToggleRememberLast = {
                                    val current =
                                        runBlocking { settings.shouldRememberLast().first() }
                                    runBlocking { settings.setRememberLast(!current) }
                                },
                                onAccentSelected = { color ->
                                    runBlocking { settings.setAccent(color) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.ADD_PLAYLIST) {
                            AddPlaylistScreen(
                                onPlaylistAdded = { url ->
                                    channels = runBlocking { PlaylistSource.loadFromUrl(url) }
                                    navController.popBackStack()
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
