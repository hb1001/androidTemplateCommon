plugins {
    id("android-library-convention")
}

android {
    namespace = "com.template.data.datastore"
}

dependencies {
    // DataStore
    implementation(libs.datastore.preferences)

    // Hilt, Coroutines 等
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.core.ktx)
}