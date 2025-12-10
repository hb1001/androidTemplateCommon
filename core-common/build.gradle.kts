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
    implementation(libs.androidx.exifinterface)
    // 设备兼容框架：https://github.com/getActivity/DeviceCompat
    implementation(libs.devicecompat)
// 权限请求框架：https://github.com/getActivity/XXPermissions
    implementation(libs.xxpermissions)
}