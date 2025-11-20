// File: core-ui/build.gradle.kts

plugins {
    // 1. 应用我们自定义的约定插件，一行搞定大部分Android配置
    id("your.project.android.library")
}

android {
    // 2. 只需要声明本模块特有的配置
    namespace = "com.template.core.ui"

    // 3. 开启Compose功能
    buildFeatures {
        compose = true
    }

    // 4. 指定Compose编译器版本，使其与Kotlin版本兼容
    //    这个版本号来自我们之前在 toml 文件中定义的 composeCompiler
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // 5. 添加Compose相关的核心依赖
    // 使用BOM (Bill of Materials) 来统一管理Compose库的版本
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    // tooling-preview 依赖可以让我们在Android Studio中预览Compose UI
    implementation(libs.androidx.compose.ui.tooling.preview)
    // tooling 依赖提供了额外的工具支持，只在debug构建时需要
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.core.ktx)


    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.compose.material)

}