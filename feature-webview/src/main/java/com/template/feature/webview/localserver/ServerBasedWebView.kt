package com.template.feature.webview.localserver

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.webkit.*

class ServerBasedWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) {

    var onProgressChanged: ((Int) -> Unit)? = null

    init {
        initWebSettings()
        this.webViewClient = SimpleClient()
        this.webChromeClient = SimpleChromeClient()
    }

    private fun initWebSettings() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(false)
            builtInZoomControls = false
            displayZoomControls = false

            // 允许混合内容（防止某些图片加载失败）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    private inner class SimpleClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()

            // === 关键修改 ===
            // 只要是 localhost 的链接，全部返回 false，让 WebView 自己处理
            // 这样 WebView 才知道 "我跳转了新页面"，并把旧页面压入历史栈
            if (url.startsWith("http://localhost") ||
                url.startsWith("http://127.0.0.1")) {
                return false
            }

            // 其他外部链接（百度、微信等），调用外部浏览器打开
            return try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
                true
            } catch (e: Exception) {
                true
            }
        }
    }

    private inner class SimpleChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChanged?.invoke(newProgress)
        }
    }
}