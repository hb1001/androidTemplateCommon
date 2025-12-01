// File: build-logic/build.gradle.kts
import org.gradle.kotlin.dsl.version
import org.gradle.kotlin.dsl.register

plugins {
    `kotlin-dsl`
}

group = "com.template.buildlogic"

// 定义版本目录扩展，方便在插件中使用 libs
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    // -----------------------------------------------------------------------
    // 核心修复点：使用 implementation 并手动拼接坐标 + findVersion
    // -----------------------------------------------------------------------

    // 1. Android Gradle Plugin (AGP) 的 Jar 包
    // 对应 libs.versions.toml 中的 [versions] agp = "..."
    implementation("com.android.tools.build:gradle:${libs.findVersion("agp").get()}")

    // 2. Kotlin Gradle Plugin (KGP) 的 Jar 包
    // 对应 libs.versions.toml 中的 [versions] kotlin = "..."
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.findVersion("kotlin").get()}")

    // 3. Compose Compiler Plugin (可选，如果你的插件里没有直接引用它的类，可以不加)
    // 通常 Convention Plugin 只需要上面两个就能配置 Compose 了
    // implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:${libs.findVersion("kotlin").get()}")
}


gradlePlugin {
    plugins {
        // 1. Android Application 约定 (App 模块通用配置)
        register("androidApplication") {
            id = "your.project.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        // 2. Android Library 约定 (普通 Library 通用配置)
        register("androidLibrary") {
            id = "your.project.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        // 3. Android Compose 约定 (UI 配置)
        register("androidCompose") {
            id = "your.project.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        // 4. Hilt 约定 (依赖注入配置 - 新增)
        register("androidHilt") {
            id = "your.project.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        // 5. Feature 约定 (聚合插件)
        register("androidFeature") {
            id = "your.project.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}