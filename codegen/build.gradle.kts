plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

//repositories {
//    google()
//    mavenCentral()
//}

//java {
//    sourceCompatibility = JavaVersion.VERSION_21
//    targetCompatibility = JavaVersion.VERSION_21
//}

dependencies {
    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // KotlinPoet - 代码生成
    implementation("com.squareup:kotlinpoet:1.16.0")

    // ViewModel Kotlin 扩展
//    implementation(libs.lifecycle.viewmodel.ktx)

    // 仅供编译识别 Compose API，不参与打包
//    compileOnly(platform("androidx.compose:compose-bom:2024.09.00"))
//    compileOnly("androidx.compose.ui:ui")
//    compileOnly("androidx.compose.foundation:foundation")
//    compileOnly("androidx.compose.material3:material3")
//    compileOnly("androidx.lifecycle:lifecycle-viewmodel-compose")

//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
}