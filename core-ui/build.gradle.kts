plugins {
    id("your.project.android.library")
    id("your.project.android.compose") // 自动添加 Compose 依赖
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.template.core.ui"
}

dependencies {
    implementation(project(":core-navigation"))

    // 额外的 UI 库
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material) // Material 2

    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    implementation(libs.kotlinx.serialization.json)
}