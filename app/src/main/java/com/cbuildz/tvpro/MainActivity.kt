package com.cbuildz.tvpro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cbuildz.tvpro.data.SettingsDataStore
import com.cbuildz.tvpro.ui.theme.AppTheme
import com.cbuildz.tvpro.ui.screens.SettingsScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val playlistSource = PlaylistSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavHost(playlistSource)
            }
        }
    }
}

object Routes {
    const val HOME = "home"
    const val ADD_PLAYLIST = "playlist/add"
    const val CHANNELS = "channels"
    const val FAVORITES = "favorites"
    const val PLAYER = "player"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavHost(playlistSource: PlaylistSource) {
    val nav = rememberNavController()
    val context = LocalContext.current
    val ds = remember { SettingsDataStore(context) }
    val scope = rememberCoroutineScope()

    // Store favorites as URL strings
    val favFlow = ds.getFavorites().collectAsState(initial = emptySet())
    var favorites by remember { mutableStateOf(favFlow.value.toMutableSet()) }

    // Last channel
    val lastChannelFlow = ds.getLastChannel().collectAsState(initial = null)
    val rememberLastFlow = ds.shouldRememberLast().collectAsState(initial = true)

    // Channels
    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
    var pendingUrl by remember { mutableStateOf<String?>(null) }

    // Sync DataStore → local state
    LaunchedEffect(favFlow.value) {
        favorites = favFlow.value.toMutableSet()
    }

    // Load playlist when URL changes
    LaunchedEffect(pendingUrl) {
        pendingUrl?.let { url ->
            playlistSource.loadFromUrl(url)
            channels = playlistSource.getChannels()
            pendingUrl = null
            nav.navigate(Routes.CHANNELS)
        }
    }

    // Auto-resume last channel if enabled
    LaunchedEffect(lastChannelFlow.value, rememberLastFlow.value) {
        if (rememberLastFlow.value) {
            lastChannelFlow.value?.let { url ->
                nav.navigate("${Routes.PLAYER}?url=${Uri.encode(url)}")
            }
        }
    }

    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                nav = nav,
                onAddPlaylist = { nav.navigate(Routes.ADD_PLAYLIST) },
                onPlayTest = {
                    val url = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
                    nav.navigate("${Routes.PLAYER}?url=${Uri.encode(url)}")
                }
            )
        }

        composable(Routes.ADD_PLAYLIST) {
            AddPlaylistScreen(
                onPlaylistAdded = { url -> pendingUrl = url },
                onBack = { nav.popBackStack() }
            )
        }

        composable(Routes.CHANNELS) {
            // Map favorites (URLs) back to Channels
            val favChannels = channels.filter { favorites.contains(it.url) }

            ChannelListScreen(
                channels = channels,
                favorites = favChannels.toSet(),
                onToggleFavorite = { c ->
                    val updated = favorites.toMutableSet()
                    if (updated.contains(c.url)) updated.remove(c.url) else updated.add(c.url)
                    favorites = updated
                    scope.launch { ds.saveFavorites(updated) }
                },
                onChannelSelected = { c ->
                    scope.launch { ds.saveLastChannel(c.url) }
                    nav.navigate("${Routes.PLAYER}?url=${Uri.encode(c.url)}")
                },
                onBack = { nav.popBackStack() },
                onFavorites = { nav.navigate(Routes.FAVORITES) }
            )
        }

        composable(Routes.FAVORITES) {
            val favChannels = channels.filter { favorites.contains(it.url) }

            ChannelListScreen(
                channels = favChannels,
                favorites = favChannels.toSet(),
                onToggleFavorite = { c ->
                    val updated = favorites.toMutableSet()
                    if (updated.contains(c.url)) updated.remove(c.url) else updated.add(c.url)
                    favorites = updated
                    scope.launch { ds.saveFavorites(updated) }
                },
                onChannelSelected = { c ->
                    scope.launch { ds.saveLastChannel(c.url) }
                    nav.navigate("${Routes.PLAYER}?url=${Uri.encode(c.url)}")
                },
                onBack = { nav.popBackStack() },
                onFavorites = { /* already here */ }
            )
        }

        composable(
            route = "${Routes.PLAYER}?url={url}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url")?.let { Uri.decode(it) }
            PlayerScreen(url ?: "")
        }

        composable(Routes.SETTINGS) {
            val rememberLast by ds.shouldRememberLast().collectAsState(initial = true)
            SettingsScreen(
                rememberLast = rememberLast,
                onToggleRememberLast = {
                    scope.launch { ds.setRememberLast(!rememberLast) }
                },
                onAccentSelected = { color ->
                    scope.launch { ds.setAccent(color) }
                },
                onBack = { nav.popBackStack() }
            )
        }
    }
}
