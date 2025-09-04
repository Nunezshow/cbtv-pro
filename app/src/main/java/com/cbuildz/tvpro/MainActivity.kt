package com.cbuildz.tvpro

import android.net.Uri
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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsDataStore(this)

        setContent {
            // observe accent from settings for theming
            val accent by settings.getAccent().collectAsState(initial = "cyan")

            AppTheme(accent = accent) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val nav = rememberNavController()
                    val scope = rememberCoroutineScope()

                    // Keep the loaded channel list in memory
                    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }

                    // Observe current playlist URL and (re)load channels when it changes
                    val playlistUrl by settings.getPlaylistUrl().collectAsState(initial = "")
                    LaunchedEffect(playlistUrl) {
                        if (playlistUrl.isNotBlank()) {
                            channels = try {
                                PlaylistSource.loadFromUrl(playlistUrl)
                            } catch (_: Exception) {
                                emptyList()
                            }
                        } else {
                            channels = emptyList()
                        }
                    }

                    // Observe favorites for quick toggles
                    val favorites by settings.getFavorites().collectAsState(initial = emptySet())

                    NavHost(navController = nav, startDestination = Routes.HOME) {

                        composable(Routes.HOME) {
                            HomeScreen(
                                onNavigate = { route -> nav.navigate(route) },
                                onPlayTest = {
                                    nav.navigate("${Routes.PLAYER}/${TEST_URL.encodeForRoute()}")
                                }
                            )
                        }

                        composable(Routes.ADD_PLAYLIST) {
                            AddPlaylistScreen(
                                onPlaylistAdded = { url ->
                                    scope.launch { settings.savePlaylistUrl(url.trim()) }
                                    // Go to channels after saving
                                    nav.popBackStack()
                                    nav.navigate(Routes.CHANNELS)
                                },
                                onBack = { nav.popBackStack() }
                            )
                        }

                        composable(Routes.CHANNELS) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = favorites, // set of urls
                                onToggleFavorite = { ch ->
                                    val updated = if (favorites.contains(ch.url)) {
                                        favorites - ch.url
                                    } else {
                                        favorites + ch.url
                                    }
                                    scope.launch { settings.saveFavorites(updated) }
                                },
                                onChannelSelected = { ch ->
                                    nav.navigate("${Routes.PLAYER}/${ch.url.encodeForRoute()}")
                                },
                                onBack = { nav.popBackStack() },
                                onFavorites = { nav.navigate(Routes.FAVORITES) }
                            )
                        }

                        composable(
                            route = "${Routes.PLAYER}/{u}",
                            arguments = listOf(
                                navArgument("u") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val decodedUrl = backStackEntry
                                .arguments?.getString("u")
                                ?.decodeFromRoute()
                                ?: return@composable
                            PlayerScreen(url = decodedUrl)
                        }

                        composable(Routes.SETTINGS) {
                            val rememberLast by settings.shouldRememberLast().collectAsState(initial = false)
                            SettingsScreen(
                                rememberLast = rememberLast,
                                onToggleRememberLast = {
                                    scope.launch { settings.setRememberLast(!rememberLast) }
                                },
                                onAccentSelected = { color ->
                                    scope.launch { settings.setAccent(color) }
                                },
                                onBack = { nav.popBackStack() }
                            )
                        }

                        composable(Routes.FAVORITES) {
                            val favUrls by settings.getFavorites().collectAsState(initial = emptySet())
                            val favChannels = channels.filter { favUrls.contains(it.url) }

                            FavoritesScreen(
                                channels = favChannels,
                                onChannelClick = { ch ->
                                    nav.navigate("${Routes.PLAYER}/${ch.url.encodeForRoute()}")
                                },
                                onBack = { nav.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

private const val TEST_URL =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.m3u8"

// Simple helpers to safely pass URLs through the Nav route
private fun String.encodeForRoute(): String = Uri.encode(this)
private fun String.decodeFromRoute(): String = Uri.decode(this)
