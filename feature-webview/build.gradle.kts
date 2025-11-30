plugins {
    id("your.project.android.feature") // 只引入这一个插件
}

android {
    // 唯一必须保留的是 namespace，因为它必须唯一
    namespace = "com.template.feature.webview"
}

dependencies {
    implementation(libs.tbs.sdk)
    implementation(libs.bugly.crashreport)
    implementation("androidx.webkit:webkit:1.9.0")
    implementation("org.nanohttpd:nanohttpd:2.3.1")
// 必须添加这个库

}