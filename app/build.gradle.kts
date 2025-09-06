plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.cbuildz.tvpro"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cbuildz.tvpro"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1"
    }

    buildFeatures { compose = true }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    packaging {
        resources {
            excludes += setOf("META-INF/DEPENDENCIES", "META-INF/NOTICE", "META-INF/LICENSE")
        }
    }
}

dependencies {
    // Compose BOM + core UI
    implementation(platform("androidx.compose:compose-bom:2025.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.9.2")

    // Material 3 for Compose
    implementation("androidx.compose.material3:material3:1.3.0")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")

    // TV Compose (weâ€™re using TV buttons; list is standard LazyColumn for now)
    implementation("androidx.tv:tv-foundation:1.0.0-alpha12")
    implementation("androidx.tv:tv-material:1.0.1")

    // Media3 / ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.8.0")
    implementation("androidx.media3:media3-ui:1.8.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.8.0")

    // DataStore (Preferences)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

}
