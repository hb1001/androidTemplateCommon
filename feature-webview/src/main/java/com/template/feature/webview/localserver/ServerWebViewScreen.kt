package com.template.feature.webview.localserver

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.feature.webview.WebViewEvent
import com.template.feature.webview.WebViewViewModel

@Composable
fun ServerWebViewScreen(
    onExitPage: () -> Unit,
    viewModel: WebViewViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Server 控制
    val server = remember { LocalWebServer(context, 8080) }
    var isServerReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        try {
            if (!server.isAlive) {
                server.start()
            }
            isServerReady = true
            Log.i("ServerWebView", "Server started at http://localhost:8080/")
        } catch (e: Exception) {
            Log.e("ServerWebView", "Failed to start server", e)
        }
        onDispose {
            server.stop()
            Log.i("ServerWebView", "Server stopped")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is WebViewEvent.ExitPage -> onExitPage()
            }
        }
    }

    // 引用 WebView
    var webViewInstance by remember { mutableStateOf<ServerBasedWebView?>(null) }

    // 【修复返回键】
    BackHandler(enabled = true) {
        val webView = webViewInstance
        // 检查 WebView 是否可以后退
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            // 不能后退了（到了首页），则退出 Activity
            viewModel.exitPage()
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (isServerReady) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    factory = { ctx ->
                        ServerBasedWebView(ctx).apply {
                            onProgressChanged = viewModel::updateProgress

                            // 【关键修改】
                            // 不要加 index.html！只加载根目录。
                            // 服务器会自动把 "/" 映射给 index.html 内容，
                            // 这样前端 Router 看到的路径就是 "/"，就能正常匹配了。
                            loadUrl("http://localhost:8080/")

                            webViewInstance = this
                        }
                    }
                )
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

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