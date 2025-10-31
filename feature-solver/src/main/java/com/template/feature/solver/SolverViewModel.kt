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
    private val solverRepository: SolverRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolverUiState())
    val uiState = _uiState.asStateFlow()

    fun onImageSelected(imageFile: File) {
        val userMessageId = UUID.randomUUID().toString()
        val systemMessageId = UUID.randomUUID().toString()

        // 1. 立即显示用户图片和 "正在识别" 占位符
        _uiState.update {
            it.copy(
                messages = it.messages +
                        ChatMessage.UserMessage(userMessageId, imageFile) +
                        ChatMessage.SystemMessage(systemMessageId, "正在识别中...", isLoading = true)
            )
        }

        // 2. 调用 Repository
        solverRepository.solveQuestion(imageFile)
            .onEach { result ->
                val newSystemMessage = when (result) {
                    is Result.Loading -> null // 已在UI上处理
                    is Result.Success -> ChatMessage.SystemMessage(systemMessageId, result.data)
                    is Result.Error -> ChatMessage.SystemMessage(systemMessageId, result.message ?: "识别失败，请重试", isError = true)
                }

                if (newSystemMessage != null) {
                    // 3. 替换占位符消息
                    _uiState.update { currentState ->
                        val updatedMessages = currentState.messages.map {
                            if (it.id == systemMessageId) newSystemMessage else it
                        }
                        currentState.copy(messages = updatedMessages)
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}