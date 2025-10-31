package com.template.feature.solver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.result.Result
import com.template.data.repository.SolverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.io.File
import java.util.UUID
import javax.inject.Inject
import com.template.core.common.utils.ImageCompressor // <-- 导入
import kotlinx.coroutines.launch // <-- 导入 launch

// 定义聊天消息的数据结构
sealed interface ChatMessage {
    val id: String
    data class UserMessage(override val id: String, val imageFile: File) : ChatMessage
    data class SystemMessage(override val id: String, val text: String, val isLoading: Boolean = false, val isError: Boolean = false) : ChatMessage
}

data class SolverUiState(
    val messages: List<ChatMessage> = emptyList()
)

@HiltViewModel
class SolverViewModel @Inject constructor(
    private val solverRepository: SolverRepository,
    private val imageCompressor: ImageCompressor // <-- 注入 ImageCompressor
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolverUiState())
    val uiState = _uiState.asStateFlow()

    fun onImageSelected(imageFile: File) {
        val userMessageId = UUID.randomUUID().toString()
        val systemMessageId = UUID.randomUUID().toString()

        // 1. 立即显示用户选择的 *原始* 图片和 "正在处理..." 占位符
        _uiState.update {
            it.copy(
                messages = it.messages +
                        ChatMessage.UserMessage(userMessageId, imageFile) +
                        ChatMessage.SystemMessage(
                            systemMessageId,
                            "正在处理图片...",
                            isLoading = true
                        )
            )
        }

        // 2. 在后台启动一个新协程来处理压缩和上传
        viewModelScope.launch {
            // --- 压缩步骤 ---
            val compressedFile = imageCompressor.compress(imageFile)
            // --- 压缩结束 ---

            // 更新 UI 状态为 "正在识别中..."
            _uiState.update { currentState ->
                val updatedMessages = currentState.messages.map { msg ->
                    if (msg.id == systemMessageId) {
                        (msg as ChatMessage.SystemMessage).copy(text = "正在识别中...")
                    } else {
                        msg
                    }
                }
                currentState.copy(messages = updatedMessages)
            }

            // 3. 使用 *压缩后* 的文件调用 Repository
            solverRepository.solveQuestion(compressedFile)
                .onEach { result ->
                    val newSystemMessage = when (result) {
                        // ... 逻辑不变
                        is Result.Loading -> null
                        is Result.Success -> ChatMessage.SystemMessage(systemMessageId, result.data)
                        is Result.Error -> ChatMessage.SystemMessage(
                            systemMessageId,
                            result.message ?: "识别失败，请重试",
                            isError = true
                        )
                    }

                    if (newSystemMessage != null) {
                        _uiState.update { currentState ->
                            val updatedMessages = currentState.messages.map {
                                if (it.id == systemMessageId) newSystemMessage else it
                            }
                            currentState.copy(messages = updatedMessages)
                        }
                    }
                }
                .launchIn(viewModelScope) // 注意：这里会创建一个新的子协程
        }
    }
}