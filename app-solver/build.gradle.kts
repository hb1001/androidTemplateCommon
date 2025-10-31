plugins {
    alias(libs.plugins.android.application) // <-- 直接应用 application 插件
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.template.app.solver"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.template.app.solver"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17 // <-- 与项目统一
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17" // <-- 与项目统一
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // 依赖所有需要的模块
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))
    implementation(project(":data-network"))
    implementation(project(":data-repository"))
    implementation(project(":feature-solver"))

    // Hilt
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)

    // AndroidX & Compose (只保留必要的)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
//    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")



    implementation(libs.timber)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}