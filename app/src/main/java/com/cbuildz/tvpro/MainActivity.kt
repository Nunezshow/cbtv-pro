package com.cbuildz.tvpro

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
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
            val accent by settings.getAccent().collectAsState(initial = "cyan")
            val rememberLast by settings.shouldRememberLast().collectAsState(initial = true)
            val favoritesUrls by settings.getFavorites().collectAsState(initial = emptySet())
            val playlistUrls by settings.getPlaylistUrls().collectAsState(initial = emptySet())

            var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
            val scope = rememberCoroutineScope()
            val nav = rememberNavController()

            // Reload channels whenever playlist set changes
            LaunchedEffect(playlistUrls) {
                channels = emptyList()
                for (u in playlistUrls) {
                    try {
                        val loaded = PlaylistSource.loadFromUrl(u)
                        channels = channels + loaded
                    } catch (t: Throwable) {
                        Log.e("MainActivity", "Failed to load playlist: $u", t)
                    }
                }
            }

            AppTheme(accent = accent) {
                NavHost(navController = nav, startDestination = Routes.HOME) {

                    composable(Routes.HOME) {
                        HomeScreen(
                            onAddPlaylist = { nav.navigate(Routes.ADD_PLAYLIST) },
                            onPlayTest = {
                                // Known good HLS test stream
                                val test = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
                                nav.navigate("${Routes.PLAYER}/${Uri.encode(test)}")
                            },
                            onNavigate = { route -> nav.navigate(route) }
                        )
                    }

                    composable(Routes.ADD_PLAYLIST) {
                        AddPlaylistScreen(
                            onPlaylistAdded = { url ->
                                scope.launch {
                                    settings.addPlaylistUrl(url)
                                }
                                // Go to channel list so the user sees the result
                                nav.popBackStack()
                                nav.navigate(Routes.CHANNEL_LIST)
                            },
                            onBack = { nav.popBackStack() }
                        )
                    }

                    composable(Routes.CHANNEL_LIST) {
                        val favoriteChannels: Set<Channel> =
                            channels.filter { favoritesUrls.contains(it.url) }.toSet()

                        ChannelListScreen(
                            channels = channels,
                            favorites = favoriteChannels,
                            onToggleFavorite = { ch ->
                                val newSet = favoritesUrls.toMutableSet()
                                if (newSet.contains(ch.url)) newSet.remove(ch.url) else newSet.add(ch.url)
                                scope.launch { settings.saveFavorites(newSet) }
                            },
                            onChannelSelected = { ch ->
                                if (rememberLast) {
                                    scope.launch { settings.saveLastChannel(ch.url) }
                                }
                                nav.navigate("${Routes.PLAYER}/${Uri.encode(ch.url)}")
                            },
                            onBack = { nav.popBackStack() },
                            onFavorites = { nav.navigate(Routes.FAVORITES) }
                        )
                    }

                    composable(
                        route = "${Routes.PLAYER}/{url}",
                        arguments = listOf(navArgument("url") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val encoded = backStackEntry.arguments?.getString("url") ?: return@composable
                        PlayerScreen(url = Uri.decode(encoded))
                    }

                    composable(Routes.FAVORITES) {
                        val favsOnly = channels.filter { favoritesUrls.contains(it.url) }
                        ChannelListScreen(
                            channels = favsOnly,
                            favorites = favsOnly.toSet(),
                            onToggleFavorite = { ch ->
                                val newSet = favoritesUrls.toMutableSet()
                                if (newSet.contains(ch.url)) newSet.remove(ch.url) else newSet.add(ch.url)
                                scope.launch { settings.saveFavorites(newSet) }
                            },
                            onChannelSelected = { ch ->
                                if (rememberLast) scope.launch { settings.saveLastChannel(ch.url) }
                                nav.navigate("${Routes.PLAYER}/${Uri.encode(ch.url)}")
                            },
                            onBack = { nav.popBackStack() },
                            onFavorites = { /* already on favorites */ }
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
                }
            }
        }
    }
}
