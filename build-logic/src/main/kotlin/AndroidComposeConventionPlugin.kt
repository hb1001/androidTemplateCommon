import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.getByType

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
            // 定义一个辅助函数来配置通用的 CommonExtension
            fun configureCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
                commonExtension.apply {
                    buildFeatures {
                        compose = true
                    }
//                    composeOptions {
//                        kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
//                    }
                }
            }

            // -----------------------------------------------------------------------
            // 核心修复：分别查找 LibraryExtension 或 ApplicationExtension
            // -----------------------------------------------------------------------

            // 1. 尝试作为 Library 处理
            val libraryExtension = extensions.findByType<LibraryExtension>()
            if (libraryExtension != null) {
                configureCompose(libraryExtension)
            } else {
                // 2. 尝试作为 Application 处理
                val applicationExtension = extensions.findByType<ApplicationExtension>()
                if (applicationExtension != null) {
                    configureCompose(applicationExtension)
                } else {
                    // 如果都找不到，说明 Android 插件还没应用，或者顺序错了
                    throw org.gradle.api.GradleException(
                        "AndroidComposeConventionPlugin could not find an Android extension. " +
                                "Ensure 'com.android.application' or 'com.android.library' is applied BEFORE this plugin."
                    )
                }
            }

            // 依赖配置保持不变
            dependencies {
                val bom = libs.findLibrary("androidx.compose.bom").get()
                add("implementation", platform(bom))
                add("androidTestImplementation", platform(bom))

                add("implementation", libs.findLibrary("androidx.compose.ui").get())
                add("implementation", libs.findLibrary("androidx.compose.ui.graphics").get())
                add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                add("implementation", libs.findLibrary("androidx.compose.material3").get())


                // 引入 Icons 对象
                add("implementation", libs.findLibrary("androidx.compose.material.icons.core").get())
                // 引入扩展图标 (如 Filled.MoreVert)
                add("implementation", libs.findLibrary("androidx.compose.material.icons.extended").get())

                add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())

                add("implementation", libs.findLibrary("androidx.activity.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtime.compose").get())
            }
        }
    }
}