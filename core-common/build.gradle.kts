// File: core-common/build.gradle.kts

plugins {
    // 应用我们的约定插件
    id("android-library-convention")
    // 应用 Hilt 插件
    alias(libs.plugins.hilt)
    // Hilt 需要 kapt
    id("kotlin-kapt")
}

android {
    namespace = "com.template.core.common"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // 依赖 Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // 依赖 Timber 日志库
    implementation(libs.timber)

    // 依赖协程核心库
    implementation(libs.kotlinx.coroutines.core)

    // 这个模块也可能需要 core-ktx
    implementation(libs.androidx.core.ktx)

    implementation("androidx.exifinterface:exifinterface:1.3.7")
}

// 允许 kapt 引用生成的代码
kapt {
    correctErrorTypes = true
}