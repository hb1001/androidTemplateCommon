// codegen/build.gradle.kts

// 1. 修改插件：将 "kotlin.jvm" 替换为 "android.library" 和 "kotlin.android"
//    同时添加 application 插件，以便后续通过命令行执行 main 函数
plugins {
    alias(libs.plugins.android.library)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23" // 序列化插件需要版本号
    alias(libs.plugins.kotlin.android)
//    id("application")
}

// 2. 添加 android 配置块，这是 Android 模块的核心
android {
    // 必须提供一个唯一的命名空间
    namespace = "com.template.codegen"
    compileSdk = 34 // 建议与 app 模块保持一致

    defaultConfig {
        minSdk = 24 // 作为一个库，可以设置一个基础的 minSdk
    }

    // 这里的 compileOptions 替代了之前的 java { ... } 块
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // 为 Kotlin 编译器指定 JVM 目标版本
    kotlinOptions {
        jvmTarget = "21"
    }

    // 作为一个非UI库，我们可以关闭 buildFeatures 来加速构建
    buildFeatures {
//        res = false
        buildConfig = false
        androidResources = false
        compose = false // 这个模块本身不包含 @Composable UI
    }
}

// 3. 删除 repositories 和 java 代码块
//    - repositories: Android 模块会自动从 settings.gradle.kts 获取仓库信息
//    - java: 已被 android.compileOptions 替代

// 4. 保持 dependencies 块不变，但建议使用 libs 别名
dependencies {
    // Kotlinx Serialization
    // 您已经定义了：kotlinx-serialization-json = { ... }
    implementation(libs.kotlinx.serialization.json)

    // KotlinPoet - 代码生成 (这个不在您的 toml 文件中，所以我们保持原样)
    implementation("com.squareup:kotlinpoet:1.16.0")

    // ViewModel Kotlin 扩展
    // 您已经定义了：androidx-lifecycle-viewmodel-ktx = { ... }
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // 仅供编译识别 Compose API，不参与打包
    // 使用您定义的 composeBom
    compileOnly(platform(libs.androidx.compose.bom))

    // 使用 bom 中的库，无需指定版本
    // 您已经定义了：androidx-compose-ui = { ... }
    compileOnly(libs.androidx.compose.ui)

    // foundation 库没有在 toml 中定义，但 bom 会管理它的版本
    compileOnly("androidx.compose.foundation:foundation")

    // 您已经定义了：androidx-compose-material3 = { ... }
    compileOnly(libs.androidx.compose.material3)

    // lifecycle-viewmodel-compose 库没有在 toml 中定义，建议添加
    // 为了保持运行，我们暂时硬编码版本
    compileOnly("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
}

//// 5. 添加 application 配置块，为命令行执行做准备
//application {
//    // 指定 main 函数所在的类
//    mainClass.set("com.template.codegen.CodeGeneratorKt")
//}