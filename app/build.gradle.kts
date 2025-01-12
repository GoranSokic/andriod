plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mynews"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mynews"
        minSdk = 26
        targetSdk = 34
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Glide dependency
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // Retrofit dependencies
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter

    // Glide annotation processor (for Java projects)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}
