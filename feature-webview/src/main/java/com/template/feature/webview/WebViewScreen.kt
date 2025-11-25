package com.template.feature.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WebViewScreen(
    url: String,
    onExitPage: () -> Unit,
    viewModel: WebViewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 1. 监听 ViewModel 的退出事件
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is WebViewEvent.ExitPage -> onExitPage()
            }
        }
    }

    // 2. 定义一个变量来持有 WebView 引用
    var webView: WebpageAdapterWebView? by remember { mutableStateOf(null) }

    // 3. 处理返回键：直接在这里判断 WebView 是否可以返回
    BackHandler(enabled = true) {
        if (webView != null && webView!!.canGoBack()) {
            webView!!.goBack() // 如果能返回，就网页后退
        } else {
            viewModel.exitPage() // 如果不能返回，通知 ViewModel 退出页面
        }
    }
    Scaffold() {innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
// WebView
            AndroidView(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                factory = { context ->
                    WebpageAdapterWebView(context).apply {
                        // 监听进度
                        onProgressChanged = viewModel::updateProgress

                        loadUrl(url)

                        // 4. 将 View 赋值给 Compose 的 state 变量
                        webView = this
                    }
                }
            )

            // 顶部进度条
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