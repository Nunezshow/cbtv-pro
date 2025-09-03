package com.cbuildz.tvpro

sealed class NavState {
    object Home : NavState()
    object AddPlaylist : NavState()
    object ChannelList : NavState()
    object Favorites : NavState()
    data class Player(val url: String) : NavState()
}
