package ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.model.Channel

/**
 * ViewModel for managing the grid of TV channels.
 * Handles loading, filtering, and favorite status.
 */
class ChannelGridViewModel : ViewModel() {
    private val _channels = MutableLiveData<List<Channel>>()
    val channels: LiveData<List<Channel>> get() = _channels

    init {
        // TODO: Load channels from repository or datasource
        _channels.value = emptyList()
    }

    fun filterByCategory(category: String) {
        // TODO: Implement filtering logic
    }

    fun toggleFavorite(channelId: String) {
        // TODO: Implement favorite toggle logic
    }
}