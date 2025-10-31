// File: build.gradle.kts (Project Root) - 推荐最终版

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.room) apply false
    // kotlin.serialization 插件是编译器插件，不需要在这里声明
}