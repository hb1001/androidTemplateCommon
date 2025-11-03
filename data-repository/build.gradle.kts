plugins {
    id("android-library-convention")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.template.data.repository"
}

dependencies {
    // 模块依赖
    implementation(project(":core-model"))
    implementation(project(":core-common")) // 需要 Result 和 Dispatchers
    implementation(project(":data-network")) // 需要 PostApiService
    implementation(project(":data-database"))
    implementation(project(":data-datastore"))

    implementation(libs.timber)
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Coroutines & Flow
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.paging.compose)
}

kapt {
    correctErrorTypes = true
}