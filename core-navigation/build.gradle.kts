plugins {
    id("your.project.android.library")
    id("your.project.android.compose")
}

android {
    namespace = "com.template.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
}