package com.template.feature.webview.local

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.webkit.*
import androidx.webkit.WebViewAssetLoader

/**
 * 专用于加载本地 Vite/Vue/React 项目的 WebView。
 * 原理：使用 WebViewAssetLoader 拦截虚拟域名请求，映射到 assets 文件。
 */
class LocalAssetWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) {

    companion object {
        private const val TAG = "LocalAssetWebView"
        // 虚拟域名，不要改，这也是前端认为的 "Base URL"
        const val VIRTUAL_DOMAIN = "https://appassets.androidplatform.net"
    }

    // 进度回调，配合 Compose 使用
    var onProgressChanged: ((Int) -> Unit)? = null

    init {
        initWebSettings()
        this.webViewClient = LocalAssetClient()
        this.webChromeClient = UniversalWebChromeClient()
    }

    private fun initWebSettings() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true

            // 允许文件访问
            allowFileAccess = true
            allowContentAccess = true

            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(false) // 本地 App 通常不需要缩放
            builtInZoomControls = false
            displayZoomControls = false

//            allowFileAccessFromFileURLs = true
//            allowUniversalAccessFromFileURLs = true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
    }

    private inner class LocalAssetClient : WebViewClient() {

        // 配置 AssetLoader：将虚拟域名映射到 Android 的 assets 目录
        private val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("appassets.androidplatform.net") // 去掉 https://
            .addPathHandler("/", WebViewAssetLoader.AssetsPathHandler(context))
            .build()

        // === 核心拦截逻辑 ===
        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            val path = request.url.path

            // 【关键修复】解决前端路由 History 模式白屏问题
            // 前端路由请求 "/" 时，Android 默认找不到文件。
            // 我们强制把它重定向到 "index.html" 的内容，但让浏览器觉得还是在访问 "/"
            if (path == "/" || path == null) {
                return assetLoader.shouldInterceptRequest(Uri.parse("$VIRTUAL_DOMAIN/index.html"))
            }

            // 其他情况（如 /assets/index.js），正常通过 AssetLoader 加载
            return assetLoader.shouldInterceptRequest(request.url)
        }

        // 兼容旧版 API
        override fun shouldInterceptRequest(
            view: WebView,
            url: String
        ): WebResourceResponse? {
            return assetLoader.shouldInterceptRequest(Uri.parse(url))
        }

        // 处理跳转逻辑
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()

            // 1. 如果是访问我们的虚拟域名，允许在 WebView 内部加载
            if (url.startsWith(VIRTUAL_DOMAIN)) {
                return false
            }

            // 2. 如果是外部链接（百度、支付宝等），跳转外部浏览器
            return handleExternalUrl(view, url)
        }

        private fun handleExternalUrl(view: WebView, url: String): Boolean {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
                return true
            } catch (e: Exception) {
                Log.e(TAG, "无法处理外部链接: $url", e)
                return true
            }
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed() // 本地调试忽略证书错误
        }
    }

    private inner class UniversalWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChanged?.invoke(newProgress)
        }
    }
}