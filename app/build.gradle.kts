
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.10"
    id("com.google.devtools.ksp") version "2.3.11"
    id("com.google.gms.google-services") version "4.5.0"
}

android {
    namespace = "com.tecsup.pokemon"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.tecsup.pokemon"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(
        "androidx.compose.material:material-icons-extended"
    )

    // Retrofit + serialización
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation(
        "com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0"
    )
    implementation(
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3"
    )
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation(
        "com.squareup.okhttp3:logging-interceptor:4.12.0"
    )

    // Imágenes
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Lifecycle y navegación
    implementation(
        "androidx.lifecycle:lifecycle-runtime-compose:2.8.7"
    )
    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7"
    )
    implementation(
        "androidx.navigation:navigation-compose:2.8.4"
    )

    // Room
    implementation("androidx.room:room-runtime:2.8.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")

    // Firebase
    implementation(
        platform("com.google.firebase:firebase-bom:33.5.1")
    )
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1"
    )

    // Pruebas unitarias
    testImplementation(libs.junit)
    testImplementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2"
    )
    testImplementation(
        "androidx.arch.core:core-testing:2.2.0"
    )

    // Pruebas instrumentadas
    androidTestImplementation(
        platform(libs.androidx.compose.bom)
    )
    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
    )
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Herramientas de depuración
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
