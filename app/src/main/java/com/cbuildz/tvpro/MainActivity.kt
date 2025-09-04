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
import com.cbuildz.tvpro.ui.theme.TVProTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val TEST_HLS = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsDataStore(this)

        setContent {
            val accent by settings.getAccent().collectAsState(initial = "cyan")
            val rememberLast by settings.shouldRememberLast().collectAsState(initial = true)
            val favoritesUrls by settings.getFavorites().collectAsState(initial = emptySet())
            val playlists by settings.getPlaylists().collectAsState(initial = emptySet())

            var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
            val scope = rememberCoroutineScope()

            // load channels whenever playlists change (simple: first playlist if present)
            LaunchedEffect(playlists) {
                if (playlists.isNotEmpty()) {
                    val first = playlists.first()
                    channels = withContext(Dispatchers.IO) {
                        PlaylistSource.loadFromUrl(first)
                    }
                } else {
                    channels = emptyList()
                }
            }

            TVProTheme(accent = accent, darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val nav = rememberNavController()

                    NavHost(
                        navController = nav,
                        startDestination = Routes.HOME
                    ) {
                        composable(Routes.HOME) {
                            HomeScreen(
                                onNavigate = { route -> nav.navigate(route) },
                                onAddPlaylist = { nav.navigate(Routes.ADD_PLAYLIST) },
                                onPlayTest = {
                                    nav.navigate("${Routes.PLAYER}/${Uri.encode(TEST_HLS)}")
                                }
                            )
                        }

                        composable(Routes.CHANNEL_LIST) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = favoritesUrls,
                                onToggleFavorite = { channel ->
                                    scope.launch {
                                        val updated = if (favoritesUrls.contains(channel.url)) {
                                            favoritesUrls - channel.url
                                        } else {
                                            favoritesUrls + channel.url
                                        }
                                        settings.saveFavorites(updated)
                                    }
                                },
                                onChannelSelected = { channel ->
                                    scope.launch {
                                        if (rememberLast) settings.saveLastChannel(channel.url)
                                    }
                                    nav.navigate("${Routes.PLAYER}/${Uri.encode(channel.url)}")
                                },
                                onBack = { nav.popBackStack() },
                                onFavorites = { nav.navigate(Routes.FAVORITES) }
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
                            val favChannels = channels.filter { favoritesUrls.contains(it.url) }
                            ChannelListScreen(
                                channels = favChannels,
                                favorites = favoritesUrls,
                                onToggleFavorite = { channel ->
                                    scope.launch {
                                        val updated = if (favoritesUrls.contains(channel.url)) {
                                            favoritesUrls - channel.url
                                        } else {
                                            favoritesUrls + channel.url
                                        }
                                        settings.saveFavorites(updated)
                                    }
                                },
                                onChannelSelected = { channel ->
                                    scope.launch {
                                        if (rememberLast) settings.saveLastChannel(channel.url)
                                    }
                                    nav.navigate("${Routes.PLAYER}/${Uri.encode(channel.url)}")
                                },
                                onBack = { nav.popBackStack() },
                                onFavorites = { /* already here */ }
                            )
                        }

                        composable(Routes.SETTINGS) {
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

                        composable(Routes.ADD_PLAYLIST) {
                            AddPlaylistScreen(
                                onPlaylistAdded = { url ->
                                    scope.launch { settings.addPlaylist(url.trim()) }
                                    nav.popBackStack()
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
