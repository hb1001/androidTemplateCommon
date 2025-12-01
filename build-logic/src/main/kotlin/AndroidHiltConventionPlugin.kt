// File: build-logic/src/main/kotlin/AndroidHiltConventionPlugin.kt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
                // Hilt 需要 Kapt (或者 KSP)，这里保持你原有的 Kapt
                apply("org.jetbrains.kotlin.kapt")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                add("implementation", libs.findLibrary("hilt.android").get())
                add("kapt", libs.findLibrary("hilt.compiler").get())
            }

            // 配置 Kapt
            extensions.configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
                correctErrorTypes = true
            }
        }
    }
}