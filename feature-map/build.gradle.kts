plugins {
    id("your.project.android.feature") // 只引入这一个插件
}

android {
    // 唯一必须保留的是 namespace，因为它必须唯一
    namespace = "com.template.feature.atrust"
}

dependencies {
    // 这里只需要添加该 feature 特有的依赖
    // 通用的 core-ui, hilt, compose 都已经被插件自动加进去了

    // 例如：
    // implementation("com.some.third.party:sdk:1.0.0")
}