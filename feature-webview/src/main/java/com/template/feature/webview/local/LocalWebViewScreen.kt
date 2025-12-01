package com.template.feature.webview.local

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.feature.webview.WebViewEvent
import com.template.feature.webview.WebViewViewModel

@Composable
fun LocalWebViewScreen(
    onExitPage: () -> Unit,
    viewModel: WebViewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 监听退出事件
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is WebViewEvent.ExitPage -> onExitPage()
            }
        }
    }

    // 持有 WebView 引用
    var webView: LocalAssetWebView? by remember { mutableStateOf(null) }

    // 处理物理返回键
    BackHandler(enabled = true) {
        if (webView != null && webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            viewModel.exitPage()
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(innerPadding)
                ,
                factory = { context ->
                    LocalAssetWebView(context).apply {
                        // 绑定进度条
                        onProgressChanged = viewModel::updateProgress

                        // === 关键调用 ===
                        // 不要带 index.html！直接加载根目录。
                        // 因为我们在 WebViewClient 里做了拦截，请求 "/" 时会自动给 index.html 的内容。
                        // 这样前端路由(Router)看到的路径就是 "/"，就不会白屏了。
                        loadUrl(LocalAssetWebView.VIRTUAL_DOMAIN + "/")

                        webView = this
                    }
                }
            )

            // 进度条
            if (uiState.progress in 1..99) {
                LinearProgressIndicator(
                    progress = uiState.progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                )
            }
        }
    }
}