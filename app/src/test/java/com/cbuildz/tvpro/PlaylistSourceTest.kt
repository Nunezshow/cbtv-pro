diff --git a//dev/null b/app/src/test/java/com/cbuildz/tvpro/PlaylistSourceTest.kt
index 0000000000000000000000000000000000000000..8b8de38c38a5b015a0b27b727ffb92b03e1a9110 100644
--- a//dev/null
+++ b/app/src/test/java/com/cbuildz/tvpro/PlaylistSourceTest.kt
@@ -0,0 +1,37 @@
+package com.cbuildz.tvpro
+
+import org.junit.Assert.assertEquals
+import org.junit.Test
+
+class PlaylistSourceTest {
+
+    @Test
+    fun `parseM3U handles tags whitespace and url validation`() {
+        val content = """
+            #EXTM3U
+            #EXTINF:-1 tvg-id="id1" tvg-name="Channel One" tvg-chno="101" tvg-logo="logo1.png" group-title="News", Channel One
+             http://example.com/stream1 
+            #EXTINF:-1 ,Channel Two
+            rtmp://example.com/stream2
+            #EXTINF:-1 ,Invalid Channel
+            not a url
+        """.trimIndent()
+
+        val channels = PlaylistSource.parseM3U(content)
+
+        assertEquals(2, channels.size)
+
+        val first = channels[0]
+        assertEquals("Channel One", first.name)
+        assertEquals("http://example.com/stream1", first.url)
+        assertEquals("logo1.png", first.logo)
+        assertEquals("News", first.group)
+        assertEquals("id1", first.tvgId)
+        assertEquals("Channel One", first.tvgName)
+        assertEquals("101", first.tvgChno)
+
+        val second = channels[1]
+        assertEquals("Channel Two", second.name)
+        assertEquals("rtmp://example.com/stream2", second.url)
+    }
+}
