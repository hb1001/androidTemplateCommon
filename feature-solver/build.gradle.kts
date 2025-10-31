plugins {
    id("android-library-convention")
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.template.feature.solver"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
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
    // 注意：lifecycle-viewmodel-ktx 已经在你的 toml 中定义了，可以直接用
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling) // 修正：tooling 应该是 debugImplementation

    // --- 新增和修正的依赖 ---

    // 1. 为了使用 collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // 2. 为了使用 Coil 加载图片
    implementation("io.coil-kt:coil-compose:2.6.0") // 建议添加到 toml

    // 3. 为了处理权限 (Accompanist Permissions)
    // 请将下面这行添加到 gradle/libs.versions.toml
    // [versions] accompanist = "0.32.0"
    // [libraries] accompanist-permissions = { group = "com.google.accompanist", name = "accompanist-permissions", version.ref = "accompanist" }
    implementation("com.google.accompanist:accompanist-permissions:0.32.0") // 或者直接使用别名 libs.accompanist.permissions

    // 4. 为了使用 extended material icons (如果需要)
    implementation("androidx.compose.material:material-icons-extended:1.6.7") // 建议添加到 toml
}

kapt {
    correctErrorTypes = true
}