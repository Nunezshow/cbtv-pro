diff --git a/app/build.gradle.kts b/app/build.gradle.kts
index 209c7d355d141066dca6d53c754652d93c1fb751..83a4a2a337ab137d03ee9dd4acd93588a72faaeb 100644
--- a/app/build.gradle.kts
+++ b/app/build.gradle.kts
@@ -38,26 +38,27 @@ dependencies {
     implementation("androidx.compose.ui:ui-tooling-preview")
     debugImplementation("androidx.compose.ui:ui-tooling")
     implementation("androidx.activity:activity-compose:1.9.2")
 
     // Material 3 for Compose
     implementation("androidx.compose.material3:material3:1.3.0")
 
     // Navigation Compose
     implementation("androidx.navigation:navigation-compose:2.7.7")
 
     // Image loading
     implementation("io.coil-kt:coil-compose:2.6.0")
 
     // TV Compose (we’re using TV buttons; list is standard LazyColumn for now)
     implementation("androidx.tv:tv-foundation:1.0.0-alpha12")
     implementation("androidx.tv:tv-material:1.0.1")
 
     // Media3 / ExoPlayer
     implementation("androidx.media3:media3-exoplayer:1.8.0")
     implementation("androidx.media3:media3-ui:1.8.0")
     implementation("androidx.media3:media3-exoplayer-hls:1.8.0")
 
     // DataStore (Preferences)
     implementation("androidx.datastore:datastore-preferences:1.1.1")
 
+    testImplementation("junit:junit:4.13.2")
 }
