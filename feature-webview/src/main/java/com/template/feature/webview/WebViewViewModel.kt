package com.template.feature.webview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

data class WebViewUiState(
    val progress: Int = 0,
    val isFullscreen: Boolean = false,
    val canGoBack: Boolean = false
)

sealed class WebViewEvent {
    object ExitPage : WebViewEvent()
}

class WebViewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WebViewUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = Channel<WebViewEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun updateProgress(value: Int) {
        _uiState.update { it.copy(progress = value) }
    }

    fun pageFinished(url: String) {}

    fun updateCanGoBack(can: Boolean) {
        _uiState.update { it.copy(canGoBack = can) }
    }

    // Compose 调用这个处理返回
    fun handleBack(webView: WebpageAdapterWebView?) {
        if (webView != null && webView.canGoBackCustom()) {
            updateCanGoBack(webView.canGoBack())
        } else {
            _eventFlow.trySend(WebViewEvent.ExitPage)
        }
    }
}
