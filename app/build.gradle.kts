plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.sddrozdov.doskacompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sddrozdov.doskacompose"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

}

dependencies {

    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.firebase.common.ktx)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)


//    // Testing
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//        //androidTestImplementation (libs.hilt.android.testing)
//
//    //Testing coroutines
//    testImplementation (libs.kotlinx.coroutines.test)
//
//    // Mockito
//    testImplementation (libs.mockito.core)
//    testImplementation (libs.mockito.inline)
//    testImplementation (libs.kotlin.test)

    // MockK testImplementation (libs.mockk)

}
