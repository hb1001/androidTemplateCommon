plugins {
    id("your.project.android.library")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.core.common"
    buildFeatures {
        buildConfig = true // 这里显式开启，因为我们在插件里默认关闭了
    }
}

dependencies {
    // Timber, Core-KTX, Hilt 已经由插件自动添加
    implementation(libs.kotlinx.coroutines.core)
    implementation("androidx.exifinterface:exifinterface:1.3.7")
}