plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.fundflow"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fundflow"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.benchmark.macro)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Lottie
    implementation(libs.lottie)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Jetpack Compose dependencies
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.compose.ui:ui:1.0.0")
    implementation ("androidx.compose.ui:ui-text:1.0.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Jetpack Compose UI dan Material
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material3:material3:1.1.0")

    // Compose Foundation untuk elemen dasar
    implementation("androidx.compose.foundation:foundation:1.5.0")

    // Compose Runtime untuk state management
    implementation("androidx.compose.runtime:runtime:1.5.0")

    // Navigation untuk Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.2")

    // Untuk Preview di Android Studio
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.0")

    // Untuk kompatibilitas back ke versi API lebih rendah
    implementation("androidx.core:core-ktx:1.12.0")

    // Optional: Jika menggunakan ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation ("androidx.navigation:navigation-compose:2.6.0")

    implementation("com.google.android.material:material:1.6.0") // Atau versi terbaru

    implementation ("com.google.android.material:material:1.9.0")

    implementation ("com.google.android.material:material:1.8.0")

    implementation ("androidx.compose.material3:material3:1.0.1")
    implementation ("androidx.compose.material:material:1.2.1")

    implementation ("androidx.compose.material:material:1.4.3")

    // AndroidX Compose dependencies
    implementation ("androidx.compose.ui:ui:1.3.0") // Material UI
    implementation ("androidx.compose.material3:material3:1.0.0") // Material 3
    implementation ("androidx.compose.ui:ui-tooling-preview:1.3.0") // Preview
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0") // Lifecycle

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0")

    // Untuk menggunakan Lottie
    implementation ("com.airbnb.android:lottie-compose:4.2.2")

    // Jika Anda menggunakan ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")

    implementation ("androidx.compose.ui:ui:1.3.0")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha02")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.3.0")




}




