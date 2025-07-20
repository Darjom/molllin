// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Hilt Gradle Plugin (no confundir con 'dagger.hilt.android.plugin')
    id("com.google.dagger.hilt.android") version "2.47" apply false
    // Google Services (Firebase)
    id("com.google.gms.google-services") version "4.4.3" apply false
}