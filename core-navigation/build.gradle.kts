// File: core-navigation/build.gradle.kts

plugins {
    // 应用我们的约定插件
    id("your.project.android.library")
}

android {
    namespace = "com.template.core.navigation"
}

dependencies {
    // 目前没有额外依赖
    // 未来如果需要定义复杂的导航参数类型，可能会添加 navigation-common-ktx
}