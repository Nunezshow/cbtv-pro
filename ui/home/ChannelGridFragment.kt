package ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ui.components.ChannelCard
import ui.components.CategoryChips

/**
 * Kodi-style Channel Grid:
 * - Large animated channel logos
 * - Focus/selection glow
 * - Category filtering, search, favorites pinning
 */
class ChannelGridFragment : Fragment() {
    private lateinit var viewModel: ChannelGridViewModel
    private lateinit var channelRecycler: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_channel_grid, container, false)
        channelRecycler = root.findViewById(R.id.channelRecycler)
        channelRecycler.layoutManager = GridLayoutManager(context, 4) // TODO: make spanCount dynamic
        // TODO: set up adapter with animated focus
        // TODO: integrate CategoryChips and Search
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChannelGridViewModel::class.java)
        // TODO: Observe LiveData for channels, favorites, filtering, etc.
    }
}