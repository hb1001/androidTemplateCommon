plugins {
    id("your.project.android.feature")
}

android {
    namespace = "com.template.feature.home"
}

dependencies {
    // 通用依赖(Hilt, Compose, Core-UI, Data-Repo)已由 Feature 插件添加

    // 特有依赖
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material)
}