package com.cbuildz.tvpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settings = SettingsDataStore(this)

        setContent {
            AppTheme(settings = settings) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val scope = rememberCoroutineScope()

                    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
                    var favorites by remember { mutableStateOf<Set<String>>(emptySet()) }

                    // Load favorites initially
                    LaunchedEffect(Unit) {
                        favorites = settings.getFavorites().first()
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Routes.HOME
                    ) {
                        composable(Routes.HOME) {
                            HomeScreen(nav = navController)
                        }

                        composable(Routes.CHANNEL_LIST) {
                            ChannelListScreen(
                                channels = channels,
                                favorites = channels.filter { favorites.contains(it.url) }.toSet(),
                                onToggleFavorite = { channel ->
                                    val updated = if (favorites.contains(channel.url)) {
                                        favorites - channel.url
                                    } else {
                                        favorites + channel.url
                                    }
                                    favorites = updated
                                    runBlocking { settings.saveFavorites(updated) }
                                },
                                onChannelSelected = { channel ->
                                    navController.navigate("${Routes.PLAYER}/${channel.url}")
                                },
                                onBack = { navController.popBackStack() },
                                onFavorites = { navController.navigate(Routes.FAVORITES) }
                            )
                        }

                        composable(
                            route = "${Routes.PLAYER}/{url}",
                            arguments = listOf(navArgument("url") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("url") ?: return@composable
                            PlayerScreen(url = url)
                        }

                        composable(Routes.FAVORITES) {
                            FavoritesScreen(
                                channels = channels,
                                onChannelClick = { channel ->
                                    navController.navigate("${Routes.PLAYER}/${channel.url}")
                                },
                                onToggleFavorite = { channel ->
                                    val updated = if (favorites.contains(channel.url)) {
                                        favorites - channel.url
                                    } else {
                                        favorites + channel.url
                                    }
                                    favorites = updated
                                    runBlocking { settings.saveFavorites(updated) }
                                }
                            )
                        }

                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                rememberLast = runBlocking { settings.shouldRememberLast().first() },
                                onToggleRememberLast = {
                                    val current = runBlocking { settings.shouldRememberLast().first() }
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
                                    scope.launch {
                                        channels = PlaylistSource.loadFromUrl(url)
                                        navController.navigate(Routes.CHANNEL_LIST)
                                    }
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
