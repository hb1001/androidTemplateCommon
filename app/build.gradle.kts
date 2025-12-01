plugins {
    id("your.project.android.application")
    id("your.project.android.compose")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.app"
    defaultConfig {
        applicationId = "com.template.app"
        // versionCode, versionName 等已在插件中设置默认值，如需覆盖可在此处添加
    }
}

dependencies {
    // 核心依赖
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))

    // 数据层 (为了 Hilt 绑定)
    implementation(project(":data-network"))
    implementation(project(":data-repository"))
    implementation(project(":data-database"))
    implementation(project(":data-datastore"))

    // 功能模块
    implementation(project(":feature-home"))
    implementation(project(":feature-login"))
    implementation(project(":feature-login-atrust"))
    implementation(project(":feature-map"))
    implementation(project(":feature-webview"))
    implementation(project(":feature-setting"))

    // Hilt Nav (App 级路由可能需要)
    implementation(libs.hilt.navigation.compose)

    // 其他特定依赖
    implementation(libs.androidx.core.splashscreen)

    // Paging & Coil (如果 app 模块直接使用了它们，否则可以移除)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material) // for pull-refresh if needed
    implementation(libs.coil.compose)
}