package com.template.feature.webview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.ui.theme.AppTheme
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(
    url: String,
    onExitPage: () -> Unit,
    viewModel: WebViewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 监听事件
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is WebViewEvent.ExitPage -> onExitPage()
            }
        }
    }

    WebViewContent(
        uiState = uiState,
        onProgress = viewModel::updateProgress,
        onPageFinished = viewModel::pageFinished,
        onBack = viewModel::handleBack,
        url = url
    )
}


@Composable
fun WebViewContent(
    uiState: WebViewUiState,
    url: String,
    onProgress: (Int) -> Unit,
    onPageFinished: (String) -> Unit,
    onBack: (WebpageAdapterWebView?) -> Unit
) {
    var webView: WebpageAdapterWebView? by remember { mutableStateOf(null) }

    // 返回键处理
    BackHandler {
        onBack(webView)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // WebView
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebpageAdapterWebView(context).apply {

                    // 全屏容器（Compose 中需要手动建）
                    val container = FrameLayout(context)
                    container.layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.fullscreenContainer = container

                    // 加载进度
                    onProgressChanged = onProgress

                    // 页面加载完成
                    onPageFinished1 = onPageFinished

                    loadUrl(url)

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
