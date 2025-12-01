package com.template.feature.webview.server

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.webkit.*

// 注意：如果你使用的是腾讯 X5 内核，请保留原有的 X5 继承和 import；
// 这里演示使用的是标准 Android WebView，逻辑是通用的。
class WebpageAdapterWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) { // 如果是 X5，这里继承 com.tencent.smtt.sdk.WebView

    companion object {
        private const val TAG = "UniversalWebView"
    }

    // 回调
    var onProgressChanged: ((Int) -> Unit)? = null
    var onPageFinishedCallback: ((String) -> Unit)? = null

    init {
        initWebSettings()
        this.webViewClient = UniversalWebViewClient()
        this.webChromeClient = UniversalWebChromeClient()
    }

    private fun initWebSettings() {
        settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(false)

            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false

            domStorageEnabled = true
            databaseEnabled = true
            // setAppCacheEnabled(true) // API 33 已废弃，根据需要保留或删除
            cacheMode = WebSettings.LOAD_DEFAULT

            loadsImagesAutomatically = true
            blockNetworkImage = false
            mediaPlaybackRequiresUserGesture = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }

        // 如果你有 X5 的特殊配置，保留在这里
        // x5WebViewExtension?.invokeMiscMethod(...)
    }

    private inner class UniversalWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return handleUrl(view, url)
        }

        // 针对新版 API 的重载
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return handleUrl(view, request.url.toString())
        }

        private fun handleUrl(view: WebView, url: String): Boolean {
            try {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    // === 关键修改 ===
                    // 返回 false 表示让 WebView 自己处理加载（这是标准做法）。
                    // 如果你手动 view.loadUrl(url) 并且返回 true，
                    // 有时会导致历史栈(BackStack)混乱，导致 canGoBack() 失效。
                    return false
                } else {
                    // 处理外部 App 跳转 (电话、邮件、支付宝等)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.context.startActivity(intent)
                    return true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to handle scheme: $url", e)
                return true // 拦截错误，不显示错误页
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            onPageFinishedCallback?.invoke(url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed() // 注意：生产环境忽略 SSL 错误有安全风险，建议仅测试使用
        }
    }

    private inner class UniversalWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChanged?.invoke(newProgress)
        }
        // 已删除所有关于 onShowCustomView (全屏视频) 的代码
    }

    // 已删除 canGoBackCustom 方法，直接使用系统原生的 canGoBack() 和 goBack() 即可
}