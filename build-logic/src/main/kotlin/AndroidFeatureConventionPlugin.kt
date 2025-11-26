import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            pluginManager.apply {
                // 1. 应用你自己定义的基础 Library 插件 (id 需要和你在 build-logic/build.gradle.kts 中注册的一致)
                apply("your.project.android.library")

                // 2. 应用 Hilt
                apply("dagger.hilt.android.plugin")
                apply("org.jetbrains.kotlin.kapt") // 或者 ksp

                // 3. 应用上面创建的 Compose 插件 (或者直接在这里写 Compose 配置)
                apply("your.project.android.compose")
            }

            // 4. Hilt 特有的 Kapt 配置
            extensions.configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
                correctErrorTypes = true
            }

            // 5. 配置 Namespace (通常 feature 模块不建议在 Plugin 里写死 namespace，
            // 因为每个模块 namespace 必须唯一，保持在 build.gradle.kts 中声明比较好，
            // 或者可以通过路径自动生成)

            dependencies {
                // --- 统一添加 Feature 模块必用的依赖 ---

                // 模块依赖 (注意：这里使用了 project 依赖)
                add("implementation", project(":core-ui"))
                add("implementation", project(":core-common"))
                add("implementation", project(":core-navigation"))
                add("implementation", project(":data-repository"))
                add("implementation", project(":data-datastore"))

                // Hilt
                add("implementation", libs.findLibrary("hilt.android").get())
                add("kapt", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.navigation.compose").get())

                // Navigation
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())

                // 其他 feature 通用的 AndroidX 库
                add("implementation", libs.findLibrary("androidx.core.ktx").get())
            }
        }
    }
}