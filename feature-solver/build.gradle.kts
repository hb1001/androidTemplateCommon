plugins {
    id("your.project.android.feature") // 切换为 Feature 插件
}

android {
    namespace = "com.template.feature.solver"
}

dependencies {
    implementation(libs.androidx.monitor)

    // Coil (如果不通过 core-ui 传递，需要在这里添加)
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
}