// File: build-logic/src/main/kotlin/AndroidApplicationConventionPlugin.kt
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // ... ApplicationExtension 的配置 ...

            dependencies {
                // 【在这里添加 Timber】
                add("implementation", libs.findLibrary("timber").get())

                // 如果需要，也可以加 Core KTX
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = 36
                defaultConfig {
                    minSdk = 24
                    targetSdk = 34
                    versionCode = 1
                    versionName = "1.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                // Kotlin 配置通常由 AGP 自动处理，或者可以使用 KotlinAndroidProjectExtension
                // 这里如果不使用 extension，可以在 kotlinOptions 块中设置，但在 Plugin 中访问较麻烦
                // 通常 compileOptions 足够控制 JVM 版本

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}