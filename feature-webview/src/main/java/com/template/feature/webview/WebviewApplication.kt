package com.template.feature.webview

import android.content.Context
import android.util.Log
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback

object WebviewApplication {
     fun initX5(context: Context) {
        val settings = mutableMapOf<String, Any>(
            TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
            TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true,
        )
        QbSdk.initTbsSettings(settings)
        QbSdk.initX5Environment(context, object : PreInitCallback {
            override fun onCoreInitFinished() {
//                Log.i(TAG, "onCoreInitFinished")
            }

            override fun onViewInitFinished(isX5: Boolean) {
//                Log.i(TAG, "onViewInitFinished, isX5=$isX5")
            }
        })
    }
}