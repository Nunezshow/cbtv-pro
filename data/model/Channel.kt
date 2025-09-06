package data.model

/**
 * Data class representing a TV Channel.
 */
data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val streamUrl: String,
    val category: String,
    val isFavorite: Boolean = false,
    val epgId: String? = null
)