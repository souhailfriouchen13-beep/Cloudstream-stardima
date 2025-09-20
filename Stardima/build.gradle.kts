plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 33
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    namespace = "com.stardima"
}

dependencies {
    implementation("com.lagradost:cloudstream3:latest.release")
}
