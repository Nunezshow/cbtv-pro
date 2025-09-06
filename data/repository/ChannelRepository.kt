package data.repository

import data.model.Channel

/**
 * Repository responsible for providing TV Channel data.
 * Replace with actual implementation (network/database) as needed.
 */
class ChannelRepository {
    /**
     * Load all channels. Replace with actual data source.
     */
    fun getChannels(): List<Channel> {
        // TODO: Replace with network/database fetch
        return listOf(
            Channel(
                id = "1",
                name = "Demo Channel",
                logoUrl = "https://example.com/logo.png",
                streamUrl = "https://example.com/stream.m3u8",
                category = "General"
            )
        )
    }
}