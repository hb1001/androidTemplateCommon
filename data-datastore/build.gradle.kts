plugins {
    id("your.project.android.library")
    id("your.project.android.hilt")
}

android {
    namespace = "com.template.data.datastore"
}

dependencies {
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.coroutines.core)
}