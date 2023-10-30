plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("kotlinx-serialization")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.yjpapp.stockportfolio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yjpapp.stockportfolio"
        minSdk = 24
        targetSdk = 34
        versionCode = 41
        versionName = "2.2.0"

        testInstrumentationRunner = "com.yjpapp.stockportfolio.common.StockPortfolioTestRunner"

    }

    buildTypes {
        getByName("debug") {
            buildConfigField("boolean", "LOG_CAT", "true")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "LOG_CAT", "false")
        }
    }
    buildFeatures {
        dataBinding = true
        compose = true
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:data"))

    //Test Code
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.compose.ui)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestAnnotationProcessor(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    kaptAndroidTest(libs.hilt.android.compiler)
    //view
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.glide)
    //RecyclerView Animation
    implementation(libs.google.material)
    //FireBase
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation(libs.firebase.messaging)
    //Compose
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewModel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    //Compose swipeLayout
    implementation(libs.revealswipe)
    //Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.supported.picker.dialogs)
    //network
    implementation(libs.okhttp)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    //Hilt
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)
    //Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
}