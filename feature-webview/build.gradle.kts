plugins {
    id("your.project.android.feature")
}

android {
    namespace = "com.template.feature.webview"
}

dependencies {
    implementation(libs.tbs.sdk)
    implementation(libs.bugly.crashreport)
    implementation(libs.androidx.webkit)
    implementation(libs.nanohttpd)

}