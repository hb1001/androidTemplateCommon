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
            // [修改] 聊天室模式：只发送，不本地立即上屏。
            // 等收到服务器的广播消息（包含 senderId=我）时，Flow 会自动更新 UI
            repository.sendMessage(text)

            // 可以在这里加个 sending 状态，但为了跑通先简化
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