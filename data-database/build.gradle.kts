plugins {
    id("your.project.android.library")
    id("your.project.android.hilt")
    alias(libs.plugins.room) // Room 保持独立，不是每个模块都需要
}

android {
    namespace = "com.template.data.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(project(":core-model"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
}