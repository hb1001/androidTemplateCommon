plugins {
    id("your.project.android.library")
    alias(libs.plugins.hilt)
    alias(libs.plugins.room) // <-- Room 插件
    id("kotlin-kapt")
}

android {
    namespace = "com.template.data.database"
}

// Room 插件配置
room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // 模块依赖
    implementation(project(":core-model"))

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx) // 提供协程支持
    kapt(libs.room.compiler)
}

kapt {
    correctErrorTypes = true
}