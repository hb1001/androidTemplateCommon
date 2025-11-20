plugins {
    id("your.project.android.library")
    alias(libs.plugins.hilt) // <-- 1. 检查 hilt 插件
    id("kotlin-kapt") // <-- 2. 检查 kapt 插件
}

android {
    namespace = "com.template.data.datastore"
}

dependencies {
    // DataStore
    implementation(libs.datastore.preferences)

    // Hilt
    implementation(libs.hilt.android) // <-- 3. 检查 hilt-android 依赖
    kapt(libs.hilt.compiler) // <-- 4. 检查 hilt-compiler 依赖

    // Coroutines & Core
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.core.ktx)
}

kapt { // <-- 5. 检查 kapt 配置
    correctErrorTypes = true
}