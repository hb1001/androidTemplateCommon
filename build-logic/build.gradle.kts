// File: build-logic/build.gradle.kts

import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("android-library-convention") {
            id = "your.project.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "your.project.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        // 新增：Feature (聚合插件)
        register("androidFeature") {
            id = "your.project.android.feature" // 给业务模块用的 ID
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

// dependencies 块是唯一需要修改的地方
dependencies {
    // 为 implementation 提供 AGP 的完整库坐标
    implementation("com.android.tools.build:gradle:${libs.findVersion("agp").get().requiredVersion}")

    // 为 implementation 提供 Kotlin Gradle Plugin 的完整库坐标
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.findVersion("kotlin").get().requiredVersion}")
}