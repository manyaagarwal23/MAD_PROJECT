plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Firebase plugin
}

android {
    namespace = "com.example.unimingle"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.unimingle"
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
}

dependencies {
    // Firebase BOM (manages versions automatically)
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))

    // Firebase Services
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage") // For image/file uploads

    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.71828") // Correct and latest stable version

    // AndroidX and UI components
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
