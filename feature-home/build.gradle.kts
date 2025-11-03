plugins {
    id("android-library-convention")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.template.feature.home"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    // 模块依赖
    implementation(project(":core-model"))
    implementation(project(":core-ui"))
    implementation(project(":core-common"))
    implementation(project(":core-navigation")) // 需要知道自己的路由
    implementation(project(":data-repository")) // ViewModel需要Repository

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // AndroidX & Compose
    implementation(libs.androidx.core.ktx)
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2") // 使用与之前相同的版本
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navigation Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)


    // 添加 Paging 3 for Compose
    implementation(libs.androidx.paging.compose)

    // 添加 Material 2 来使用 pull-refresh 组件
    implementation(libs.androidx.compose.material)
}

kapt {
    correctErrorTypes = true
}