plugins {
    id("your.project.android.library")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    alias(libs.plugins.kotlin.serialization) // Ktor 需要序列化插件
}

android {
    namespace = "com.template.data.network"
}

dependencies {
    // 模块依赖
    implementation(project(":core-model"))
    implementation(project(":core-common")) // 需要 Timber
    implementation(project(":data-datastore"))
    implementation(libs.ktor.client.auth)
    // Hilt
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio) // CIO 引擎，适用于协程
    implementation(libs.ktor.client.okhttp) // CIO 引擎，适用于协程
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    // Timber
    implementation(libs.timber)
}

kapt {
    correctErrorTypes = true
}