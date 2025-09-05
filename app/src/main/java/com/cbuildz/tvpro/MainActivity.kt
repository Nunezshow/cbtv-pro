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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsDataStore(this)

        setContent {
            // read accent + rememberLast from DataStore
            val accent by settings.getAccent().collectAsState(initial = "cyan")
            val rememberLast by settings.shouldRememberLast().collectAsState(initial = true)
            val favorites by settings.getFavorites().collectAsState(initial = emptySet())

            TVProTheme(accentName = accent, darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()

                    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }

                    NavHost(navController = navController, startDestination = Routes.HOME) {
                        composable(Routes.HOME) {
                            HomeScreen(
                                onAddPlaylist = { navController.navigate(Routes.ADD_PLAYLIST) },
                                onPlayTest = {
                                    val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
                                    navController.navigate("${Routes.PLAYER}/${Uri.encode(url)}")
                                },
                                onBrowseChannels = { navController.navigate(Routes.CHANNEL_LIST) },
                                onSettings = { navController.navigate(Routes.SETTINGS) }
                            )
                        }

                        composable(Routes.CHANNEL_LIST) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = favorites,
                                onToggleFavorite = { ch ->
                                    val updated =
                                        if (favorites.contains(ch.url)) favorites - ch.url
                                        else favorites + ch.url
                                    scope.launch { settings.saveFavorites(updated) }
                                },
                                onChannelSelected = { ch ->
                                    scope.launch {
                                        if (rememberLast) settings.saveLastChannel(ch.url)
                                    }
                                    navController.navigate("${Routes.PLAYER}/${Uri.encode(ch.url)}")
                                },
                                onBack = { navController.popBackStack() },
                                onFavorites = { navController.navigate(Routes.FAVORITES) }
                            )
                        }

                        composable(Routes.FAVORITES) {
                            FavoritesScreen(
                                channels = channels,
                                favorites = favorites,
                                onChannelClick = { ch ->
                                    scope.launch {
                                        if (rememberLast) settings.saveLastChannel(ch.url)
                                    }
                                    navController.navigate("${Routes.PLAYER}/${Uri.encode(ch.url)}")
                                },
                                onToggleFavorite = { ch ->
                                    val updated =
                                        if (favorites.contains(ch.url)) favorites - ch.url
                                        else favorites + ch.url
                                    scope.launch { settings.saveFavorites(updated) }
                                }
                            )
                        }

                        composable(
                            route = "${Routes.PLAYER}/{url}",
                            arguments = listOf(navArgument("url") { type = NavType.StringType })
                        ) { bs ->
                            val url = bs.arguments?.getString("url")?.let { Uri.decode(it) } ?: return@composable
                            PlayerScreen(url = url)
                        }

                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                rememberLast = rememberLast,
                                onToggleRememberLast = {
                                    scope.launch { settings.setRememberLast(!rememberLast) }
                                },
                                onAccentSelected = { newAccent ->
                                    scope.launch { settings.setAccent(newAccent) }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.ADD_PLAYLIST) {
                            AddPlaylistScreen(
                                onPlaylistAdded = { url ->
                                    scope.launch {
                                        channels = PlaylistSource.loadFromUrl(url)
                                    }
                                    navController.navigate(Routes.CHANNEL_LIST)
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
