diff --git a//dev/null b/ChannelGridAdapter.kt
index 0000000000000000000000000000000000000000..56727023c9c069b4a53057c8ac9e9066e64eea7b 100644
--- a//dev/null
+++ b/ChannelGridAdapter.kt
@@ -0,0 +1,52 @@
+package ui.home
+
+import android.view.LayoutInflater
+import android.view.View
+import android.view.ViewGroup
+import android.widget.TextView
+import androidx.recyclerview.widget.RecyclerView
+import com.cbuildz.tvpro.R
+import data.model.Channel
+
+/**
+ * Simple adapter displaying a grid of channels.
+ */
+class ChannelGridAdapter(
+    private var channels: List<Channel>,
+    private val onClick: (Channel) -> Unit,
+    private val onLongClick: (Channel) -> Unit
+) : RecyclerView.Adapter<ChannelGridAdapter.ChannelViewHolder>() {
+
+    inner class ChannelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
+        private val name: TextView = view.findViewById(android.R.id.text1)
+
+        fun bind(channel: Channel) {
+            name.text = channel.name
+            itemView.setOnClickListener { onClick(channel) }
+            itemView.setOnLongClickListener {
+                onLongClick(channel)
+                true
+            }
+        }
+    }
+
+    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
+        val view = LayoutInflater.from(parent.context)
+            .inflate(android.R.layout.simple_list_item_1, parent, false)
+        return ChannelViewHolder(view)
+    }
+
+    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
+        holder.bind(channels[position])
+    }
+
+    override fun getItemCount(): Int = channels.size
+
+    /**
+     * Update the channel list displayed by the adapter.
+     */
+    fun submitList(newChannels: List<Channel>) {
+        channels = newChannels
+        notifyDataSetChanged()
+    }
+}
