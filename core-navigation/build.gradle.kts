// File: core-navigation/build.gradle.kts

plugins {
    // 应用我们的约定插件
    id("your.project.android.library")
    id("your.project.android.compose")
}

android {
    namespace = "com.template.core.navigation"
}

dependencies {

    implementation(libs.androidx.navigation.compose)
//    implementation(libs.androidx.runtime)
    // 目前没有额外依赖
    // 未来如果需要定义复杂的导航参数类型，可能会添加 navigation-common-ktx
}