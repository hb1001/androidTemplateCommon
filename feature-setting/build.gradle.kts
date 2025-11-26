plugins {
    id("your.project.android.feature") // 只引入这一个插件
}

dependencies {

}
android {
    // 唯一必须保留的是 namespace，因为它必须唯一
    namespace = "com.template.feature.setting"
}
