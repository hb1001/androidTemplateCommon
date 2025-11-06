// File: core-model/build.gradle.kts

plugins {
    // 使用统一的、更简洁的别名
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

// 统一Java版本为17
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    // 添加kotlinx.serialization的运行时库
    implementation(libs.kotlinx.serialization.json)
}