// File: build-logic/build.gradle.kts
plugins {
    `kotlin-dsl`
}

// 配置Kotlin DSL插件，使其能找到我们定义的插件
gradlePlugin {
    plugins {
        register("android-library-convention") {
            id = "android-library-convention"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}