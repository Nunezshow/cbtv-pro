diff --git a/app/src/main/java/com/cbuildz/tvpro/Channel.kt b/app/src/main/java/com/cbuildz/tvpro/Channel.kt
index b9934e52168a581074db81870f9212ef0a548e95..c28d509d1d872d30fc6b90fcf7e5bd15dfe12e6e 100644
--- a/app/src/main/java/com/cbuildz/tvpro/Channel.kt
+++ b/app/src/main/java/com/cbuildz/tvpro/Channel.kt
@@ -1,8 +1,11 @@
 package com.cbuildz.tvpro
 
 data class Channel(
     val name: String,
     val url: String,
     val logo: String? = null,
-    val group: String? = null
+    val group: String? = null,
+    val tvgId: String? = null,
+    val tvgName: String? = null,
+    val tvgChno: String? = null
 )
