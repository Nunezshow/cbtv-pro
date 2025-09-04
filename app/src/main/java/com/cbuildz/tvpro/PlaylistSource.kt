package com.cbuildz.tvpro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object PlaylistSource {

    suspend fun loadFromUrl(url: String): List<Channel> = withContext(Dispatchers.IO) {
        try {
            val content = URL(url).readText()
            parseM3U(content)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseM3U(content: String): List<Channel> {
        val lines = content.lines()
        val channels = mutableListOf<Channel>()
        var currentName = ""
        var currentLogo: String? = null
        var currentGroup: String? = null

        for (line in lines) {
            when {
                line.startsWith("#EXTINF") -> {
                    val nameRegex = Regex(",(.+)")
                    val logoRegex = Regex("tvg-logo=\"([^\"]+)\"")
                    val groupRegex = Regex("group-title=\"([^\"]+)\"")

                    currentName = nameRegex.find(line)?.groupValues?.get(1)?.trim() ?: "Unknown"
                    currentLogo = logoRegex.find(line)?.groupValues?.get(1)
                    currentGroup = groupRegex.find(line)?.groupValues?.get(1)
                }
                line.startsWith("http") -> {
                    channels.add(
                        Channel(
                            name = currentName,
                            url = line.trim(),
                            logo = currentLogo,
                            group = currentGroup
                        )
                    )
                }
            }
        }
        return channels
    }
}
