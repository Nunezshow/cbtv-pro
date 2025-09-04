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

                    // State for channels loaded from playlists
                    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }

                    // Load all playlists (dummy now, extend later)
                    LaunchedEffect(Unit) {
                        // Example load
                        // channels = PlaylistSource.loadFromUrl("http://example.com/playlist.m3u")
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME
                    ) {
                        composable(Routes.HOME) {
                            HomeScreen(
                                onAddPlaylist = { navController.navigate(Routes.ADD_PLAYLIST) },
                                onPlayTest = { navController.navigate("${Routes.PLAYER}/https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8") },
                                onBrowseChannels = { navController.navigate(Routes.CHANNEL_LIST) },
                                onSettings = { navController.navigate(Routes.SETTINGS) }
                            )
                        }

                        composable(Routes.CHANNEL_LIST) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = runBlocking { settings.getFavorites().first() },
                                onChannelSelected = { channel ->
                                    navController.navigate("${Routes.PLAYER}/${channel.url}")
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
                            arguments = listOf(
                                navArgument("url") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url") ?: return@composable
                            PlayerScreen(url = url)
                        }

                        composable(Routes.FAVORITES) {
                            FavoritesScreen(
                                channels = channels,
                                favorites = runBlocking { settings.getFavorites().first() },
                                onChannelClick = { channel ->
                                    navController.navigate("${Routes.PLAYER}/${channel.url}")
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
                                    // load channels from playlist
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
