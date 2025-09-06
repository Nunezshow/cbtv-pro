diff --git a/ui/home/ChannelGridFragment.kt b/ui/home/ChannelGridFragment.kt
index 8e5d79cd82a8366047489bda7c1c83c957d0eafd..082cf7c22b4f44d1166c1fd82d60e78a3331df31 100644
++ b/ui/home/ChannelGridFragment.kt
@@ -1,43 +1,51 @@
 package ui.home
 
 import android.os.Bundle
 import android.view.LayoutInflater
 import android.view.View
 import android.view.ViewGroup
 import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
 import androidx.recyclerview.widget.GridLayoutManager
 import androidx.recyclerview.widget.RecyclerView
import com.cbuildz.tvpro.R
import data.model.Channel
 
 /**
  * Fragment displaying a grid of TV channels.
 * Connects to [ChannelGridViewModel] and updates the adapter when
 * the channel list changes.
  */
 class ChannelGridFragment : Fragment() {
    private lateinit var viewModel: ChannelGridViewModel
     private lateinit var adapter: ChannelGridAdapter
 
     override fun onCreateView(
         inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChannelGridViewModel::class.java]
        val view = inflater.inflate(R.layout.fragment_channel_grid, container, false)
         val recyclerView = view.findViewById<RecyclerView>(R.id.channel_grid)
         recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = ChannelGridAdapter(
            emptyList(),
            onClick = { channel -> navigateToPlayer(channel) },
            onLongClick = { channel -> viewModel.toggleFavorite(channel) }
        )
         recyclerView.adapter = adapter
        return view
    }
 
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         viewModel.channels.observe(viewLifecycleOwner) { channels ->
            adapter.submitList(channels)
         }
    }
 
    private fun navigateToPlayer(channel: Channel) {
        // TODO: Implement navigation to player screen
     }
}
