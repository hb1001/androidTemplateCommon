package com.template.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.result.Result
import com.template.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // 1. 开始监听来自数据库的数据流
        postRepository.getPosts()
            .onStart { _uiState.value = _uiState.value.copy(isLoading = true) } // 开始监听时，显示加载
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Database error"
                )
            }
            .onEach { posts ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    posts = posts,
                    error = null // 成功获取数据后，清除旧的错误信息
                )
            }
            .launchIn(viewModelScope)

        // 2. 触发一次网络数据同步
        syncData()
    }

    private fun syncData() {
        viewModelScope.launch {
            // 我们不关心同步成功与否的返回值，因为UI已经通过监听数据库来更新了。
            // 但我们可以根据返回结果更新一个同步状态的标志位，比如显示一个刷新失败的toast
            val syncResult = postRepository.syncPosts()
            if (syncResult is Result.Error && _uiState.value.posts.isEmpty()) {
                // 只有当同步失败且没有任何缓存数据时，才显示错误
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = syncResult.message ?: "Failed to fetch data"
                )
            }
        }
    }
}