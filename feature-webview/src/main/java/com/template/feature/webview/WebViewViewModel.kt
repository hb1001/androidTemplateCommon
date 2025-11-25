package com.template.feature.webview

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

data class WebViewUiState(
    val progress: Int = 0
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

    // 只有当 WebView 无法返回时，Compose 层会调用这个方法来退出 Activity
    fun exitPage() {
        _eventFlow.trySend(WebViewEvent.ExitPage)
    }
}