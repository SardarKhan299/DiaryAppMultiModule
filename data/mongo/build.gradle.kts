plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.realm.kotlin")
    id ("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.mongo"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.extensionVersion
    }
}

dependencies {

    implementation (libs.desugar.jdk)
    implementation (libs.realm.sync)
    implementation (libs.coroutines.core)
    implementation (libs.core.ktx)
    implementation(libs.material3.compose)
    implementation(libs.activity.compose)
    implementation (libs.compose.tooling.preview)
    implementation (libs.coil)
    implementation (libs.coroutines.core)

    implementation (libs.room.runtime)
    implementation (libs.room.ktx)
    ksp (libs.room.compiler)

    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)

    implementation(project(":core:util"))
}