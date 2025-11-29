package com.template.generated.page.websocket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.result.Result
import com.template.core.model.SocketMessage
import com.template.data.repository.WebSocketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI 状态定义
data class WebSocketUiState(
    val messages: List<SocketMessage> = emptyList(), // 聊天记录
    val isConnected: Boolean = false,               // 连接状态
    val error: String? = null,                      // 错误信息
    val isLoading: Boolean = false                  // 是否正在连接
)

@HiltViewModel
class WebSocketViewModel @Inject constructor(
    private val repository: WebSocketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WebSocketUiState())
    val uiState: StateFlow<WebSocketUiState> = _uiState.asStateFlow()

    // 这一步决定了进入页面自动连接。
    // 如果你想手动点按钮连接，就把这个 init 块去掉，暴露一个 connect() 方法给 UI。
    init {
        connect()
    }

    private fun connect() {
        viewModelScope.launch {
            repository.connectAndObserveMessages().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        // 收到新消息 -> 追加到列表，并标记为已连接
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isConnected = true,
                                error = null,
                                messages = currentState.messages + result.data
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isConnected = false,
                                error = result.message ?: "Unknown error"
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            // 1. 本地先上屏（优化体验）
            // 注意：SocketMessage 应该在 core-model 里，这里模拟构建一个自己发的消息
            val myMessage = SocketMessage(
                content = text,
                isFromMe = true,
                timestamp = System.currentTimeMillis()
            )

            _uiState.update {
                it.copy(messages = it.messages + myMessage)
            }

            // 2. 发送网络请求
            repository.sendMessage(text)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel 销毁时断开连接
        viewModelScope.launch {
            repository.closeConnection()
        }
    }
}