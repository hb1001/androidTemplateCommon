package com.template.generated

import android.app.Application
import com.template.core.common.BuildConfig // 假设 core-common 中有
import com.template.feature.webview.WebviewApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 Timber
        // 注意: 为了能在 core-common 中访问 BuildConfig.DEBUG,
        // 你需要在 core-common/build.gradle.kts 中添加 buildFeatures { buildConfig = true }
        // 或者直接在这里判断，因为 app 模块总是知道自己是 debug 还是 release
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        WebviewApplication.initX5(this)
        Timber.d("WebviewApplication initX5")
    }
}