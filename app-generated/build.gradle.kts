plugins {
    id("your.project.android.application")
    id("your.project.android.compose")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.generated"
    defaultConfig {
        applicationId = "com.template.generated"
    }
}

dependencies {
    // 核心
    implementation(project(":core-common"))
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-navigation"))

    // 数据
    implementation(project(":data-network"))
    implementation(project(":data-repository"))
    implementation(project(":data-database"))
    implementation(project(":data-datastore"))

    // 功能
    implementation(project(":feature-home"))
    implementation(project(":feature-login"))
    implementation(project(":feature-login-atrust"))
    implementation(project(":feature-map"))
    implementation(project(":feature-webview"))
    implementation(project(":feature-setting"))

    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}