package com.template.feature.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

@SuppressLint("SetJavaScriptEnabled")
class WebpageAdapterWebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "UniversalWebView"
    }

    var fullscreenContainer: ViewGroup? = null
    var onFullscreenStateChanged: ((Boolean) -> Unit)? = null
    var onProgressChanged: ((Int) -> Unit)? = null
    var onPageFinished1: ((String) -> Unit)? = null

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
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_DEFAULT

            loadsImagesAutomatically = true
            blockNetworkImage = false
            mediaPlaybackRequiresUserGesture = false

            // === 优化点1：解决 HTTPS 网站不加载 HTTP 图片的问题 ===
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // MIXED_CONTENT_ALWAYS_ALLOW = 0
                mixedContentMode = 0
            }
        }

        val map = Bundle()
        map.putBoolean("use_behavior_optimization", true)
        if (x5WebViewExtension != null) {
            x5WebViewExtension.invokeMiscMethod("setWebSettingsExtension", map)
        }
    }

    private inner class UniversalWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // === 优化点2：处理外部 App 跳转 (电话、邮件、支付宝等) ===
            try {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url)
                    return true
                } else {
                    // 尝试调用系统 Intent 打开非 http 协议
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view.context.startActivity(intent)
                    return true
                }
            } catch (e: Exception) {
                // 防止手机没安装对应 App 导致崩溃
                Log.e(TAG, "Failed to handle scheme: $url", e)
                return true // 拦截掉，不让 WebView 显示错误页
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.i(TAG, "Page Loaded: $url")
            onPageFinished1?.invoke(url)
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.proceed()
        }
    }

    private inner class UniversalWebChromeClient : WebChromeClient() {
        private var customView: View? = null
        private var customViewCallback: IX5WebChromeClient.CustomViewCallback? = null

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            onProgressChanged?.invoke(newProgress)
        }

        override fun onShowCustomView(view: View, callback: IX5WebChromeClient.CustomViewCallback) {
            if (fullscreenContainer == null) {
                callback.onCustomViewHidden()
                return
            }
            if (customView != null) {
                callback.onCustomViewHidden()
                return
            }
            customView = view
            customViewCallback = callback
            fullscreenContainer?.visibility = VISIBLE
            fullscreenContainer?.addView(view, LayoutParams(-1, -1))
            this@WebpageAdapterWebView.visibility = INVISIBLE
            onFullscreenStateChanged?.invoke(true)
        }

        override fun onHideCustomView() {
            if (customView == null) return
            this@WebpageAdapterWebView.visibility = VISIBLE
            fullscreenContainer?.removeView(customView)
            fullscreenContainer?.visibility = GONE
            customView = null
            customViewCallback?.onCustomViewHidden()
            onFullscreenStateChanged?.invoke(false)
        }
    }

    fun canGoBackCustom(): Boolean {
        if (webChromeClient is UniversalWebChromeClient) {
            val client = webChromeClient as UniversalWebChromeClient
            if (fullscreenContainer?.visibility == VISIBLE) {
                client.onHideCustomView()
                return true
            }
        }
        return if (canGoBack()) {
            goBack()
            true
        } else {
            false
        }
    }
}