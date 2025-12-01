// File: build-logic/src/main/kotlin/AndroidFeatureConventionPlugin.kt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("your.project.android.library")
                apply("your.project.android.hilt")
                apply("your.project.android.compose")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                // Feature 模块标准依赖
                add("implementation", project(":core-model"))
                add("implementation", project(":core-ui"))
                add("implementation", project(":core-common"))
                add("implementation", project(":core-navigation"))

                // 大多数 Feature 需要访问数据层
                add("implementation", project(":data-repository"))
                add("implementation", project(":data-datastore"))

                // Feature 特有的第三方库
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
            }
        }
    }
}