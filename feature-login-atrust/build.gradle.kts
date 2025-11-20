plugins {
    id("android-library-convention")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.template.feature.atrust"
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
}

dependencies {
    // 模块依赖
    implementation(project(":core-ui"))
    implementation(project(":core-common"))
    implementation(project(":core-navigation"))
    implementation(project(":data-repository"))

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // AndroidX & Compose
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // Navigation
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
}

kapt { correctErrorTypes = true }