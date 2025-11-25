import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // 应用基础插件
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            // 配置Android Library通用设置
            extensions.configure<LibraryExtension> {
                compileSdk = 34
                defaultConfig {
                    minSdk = 24
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    // 添加 consumer-proguard-rules.pro  应该是可选的
//                    consumerProguardFiles("consumer-rules.pro")
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }
            }

            // 配置Kotlin通用设置
            extensions.configure<KotlinAndroidProjectExtension> {
                jvmToolchain(17)
            }

            // --- 新增：统一添加测试依赖 --- 暂时注释因为其他的都写了
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
//            dependencies {
//                // 单元测试
//                add("testImplementation", libs.findLibrary("junit").get())
//
//                // UI/仪器测试
//                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
//                add("androidTestImplementation", libs.findLibrary("androidx.espresso.core").get())
//            }
            dependencies{
                add("implementation", libs.findLibrary("timber").get())
            }
            // --- 新增结束 ---
        }
    }
}