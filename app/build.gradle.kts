plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id("com.google.gms.google-services")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.digitalnotebook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.digitalnotebook"
        minSdk = 26
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
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Navigate
    implementation(libs.androidx.navigation.compose)
    //
    implementation (libs.androidx.material)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation ("io.coil-kt:coil-compose:2.6.0")
    //Cloud Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.1")
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    // мнопоточность
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    // Dagger hilt
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    implementation(libs.androidx.hilt.navigation.compose)
    //
    implementation(libs.androidx.constraintlayout.compose)
    // Календарь
    implementation ("androidx.compose.material:material-icons-extended:1.7.4")
    //lottia
    implementation("com.airbnb.android:lottie-compose:5.2.0")



}

kapt {
    correctErrorTypes = true
}