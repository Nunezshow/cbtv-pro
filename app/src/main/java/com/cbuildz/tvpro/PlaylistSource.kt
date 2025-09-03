package com.cbuildz.tvpro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class PlaylistSource {
    private var channels: List<Channel> = emptyList()

    fun getChannels(): List<Channel> = channels

    suspend fun loadFromUrl(url: String) {
        channels = withContext(Dispatchers.IO) {
            parseM3U(URL(url).readText())
        }
    }

    private fun parseM3U(content: String): List<Channel> {
        val result = mutableListOf<Channel>()
        var currentName = ""
        var currentLogo: String? = null
        var currentGroup: String? = null

        content.lines().forEach { line ->
            when {
                line.startsWith("#EXTINF") -> {
                    // Example: #EXTINF:-1 tvg-logo="logo.png" group-title="News",Channel Name
                    val namePart = line.substringAfterLast(",").trim()
                    currentName = namePart
                    currentLogo = Regex("tvg-logo=\"(.*?)\"").find(line)?.groupValues?.get(1)
                    currentGroup = Regex("group-title=\"(.*?)\"").find(line)?.groupValues?.get(1)
                }
                line.startsWith("http") -> {
                    result.add(Channel(currentName, line.trim(), currentLogo, currentGroup))
                }
            }
        }
        return result
    }
}
