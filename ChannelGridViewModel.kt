diff --git a/ChannelGridViewModel.kt b/ChannelGridViewModel.kt
index e545b49b3b014cb24e47dedd5af084932b770f1d..959116e6eb53481dc0c02014c4aea39cdc8c31e2 100644
++ b/ChannelGridViewModel.kt
@@ -1,13 +1,89 @@
 package ui.home
 
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import data.model.Channel
 import data.model.Category
import data.repository.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
 
 /**
  * ViewModel for ChannelGridFragment
  * - Handles channel data, filtering, favorites, search, EPG
  */
class ChannelGridViewModel(
    private val repository: ChannelRepository = ChannelRepository()
) : ViewModel() {

    private val _allChannels = MutableStateFlow<List<Channel>>(emptyList())
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _epgInfo = MutableStateFlow<Map<String, String>>(emptyMap())
    val epgInfo: StateFlow<Map<String, String>> = _epgInfo.asStateFlow()

    private val _categoryFilter = MutableStateFlow<Category?>(null)

    init {
        loadChannels()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            val data = repository.getChannels()
            _allChannels.value = data
            _channels.value = data
            _categories.value = data.map { Category(it.category) }.distinct()
            _favorites.value = data.filter { it.isFavorite }.map { it.id }.toSet()
        }
    }

    fun toggleFavorite(channelId: String) {
        val updated = _allChannels.value.map {
            if (it.id == channelId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _allChannels.value = updated
        _favorites.value = updated.filter { it.isFavorite }.map { it.id }.toSet()
        applyFilters()
    }

    fun applyCategoryFilter(category: Category?) {
        _categoryFilter.value = category
        applyFilters()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    private fun applyFilters() {
        val query = _searchQuery.value.lowercase()
        val category = _categoryFilter.value?.name
        _channels.value = _allChannels.value.filter { channel ->
            val matchesCategory = category?.let { channel.category == it } ?: true
            val matchesQuery = channel.name.lowercase().contains(query)
            matchesCategory && matchesQuery
        }
    }

    fun refreshEpg() {
        viewModelScope.launch {
            // Placeholder for EPG fetching logic
            val info = _allChannels.value.associate { it.id to "EPG for ${it.name}" }
            _epgInfo.value = info
        }
    }
}
