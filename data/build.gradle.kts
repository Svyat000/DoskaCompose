plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.sddrozdov.data"
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
}

dependencies {

    implementation(project(":domain"))

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //Firebase
    implementation (platform(libs.firebase.bom))
    api(libs.firebase.auth.ktx)
    api(libs.firebase.database.ktx)

//  Тестирование
    androidTestImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Appwrite SDK
    api("io.appwrite:sdk-for-android:7.0.0")


//
//    implementation(libs.play.services.auth)
//
////    implementation(libs.androidx.core.ktx)
////    implementation(libs.androidx.appcompat)
//

//    implementation(libs.googleid)





}