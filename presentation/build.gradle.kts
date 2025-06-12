plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("plugin.serialization")
    id("kotlin-parcelize")
}

android {
    namespace = "com.sddrozdov.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":domain"))

// AndroidX Credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

// AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.savedstate)

// Kotlin Serialization
    implementation(libs.kotlinx.serialization)

// Jetpack Compose BOM (управление версиями Compose)
    implementation(platform(libs.androidx.compose.bom))

// Jetpack Compose UI и навигация
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui.tooling.preview.android)

// Material design
    implementation(libs.androidx.material3.android)

// Google ID (Google Sign-In)
    implementation(libs.googleid)

// Firebase Auth
    implementation(libs.firebase.auth.ktx)

// Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compiler)

// Gson для JSON сериализации
    api(libs.gson)

// Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Debugging
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}