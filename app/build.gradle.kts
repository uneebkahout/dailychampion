plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
alias(libs.plugins.google.hilt.dagger)

}

android {
    namespace = "com.lsp.dailchampion"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lsp.dailchampion"
        minSdk = 28
        targetSdk = 36
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
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.navigation3.runtime.android)
    implementation(libs.androidx.navigation3.ui.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


//     < =========== NAV  3 =========== >


    implementation(libs.androidx.navigation.compose) // stable navigation
    implementation(libs.androidx.lifecycle.viewmodel.compose) // stable ViewModel integration
    //noinspection UseTomlInstead,UseTomlInstead
    implementation("androidx.compose.material3:material3:1.3.2") // stable Material3
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2") // adaptive layouts


//     < =========== Dagger hilt  =========== >

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //     < =========== Room =========== >
    implementation(libs.androidx.room.runtime)
    //noinspection GradleDependency
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)


implementation(libs.androidx.material.icons.extended)
    implementation("androidx.compose.foundation:foundation:1.9.0")



    implementation(libs.material.dialogs.core)
    implementation(libs.material.dialogs.datetime)
}