// File: app/build.gradle.kts

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // 删除了 kotlin.compose 插件，因为它不再需要手动应用
    alias(libs.plugins.hilt) // 添加 Hilt 插件
    id("kotlin-kapt") // 添加 kapt 插件
}

android {
    namespace = "com.template.generated" // 建议使用 .app 后缀以区分
    compileSdk = 34 // 与 build-logic/convention 插件保持一致

    defaultConfig {
        applicationId = "com.template.generated"
        minSdk = 24
        targetSdk = 34 // targetSdk 最好和 compileSdk 保持一致
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
        sourceCompatibility = JavaVersion.VERSION_17 // 统一到 Java 17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true // 开启Compose
    }
    // composeOptions 会由 AGP 根据 compose = true 自动处理
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
    // 依赖所有 core 模块
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))

    // 依赖数据层 (为了Hilt能找到所有绑定)
    implementation(project(":data-network"))
    implementation(project(":data-repository"))
    implementation(project(":data-database")) // <-- 最好也加上，逻辑同上
    implementation(project(":data-datastore")) // <-- 添加这一行，这是关键，否则hilt找不大


    // 依赖功能模块
    implementation(project(":feature-home"))
    implementation(project(":feature-login"))
    implementation(project(":feature-login-atrust"))
    implementation(project(":feature-map"))
    implementation(project(":feature-webview"))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)


    implementation(libs.timber)
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.runtime)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // AndroidX & Compose
    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)


    // Navigation Compose
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2") // 使用与之前相同的版本
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)


    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // 添加 Paging 3 for Compose
    implementation(libs.androidx.paging.compose)

    // 添加 Material 2 来使用 pull-refresh 组件
    implementation(libs.androidx.compose.material)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}