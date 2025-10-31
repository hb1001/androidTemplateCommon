package com.template.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.core.common.result.Result
import com.template.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        getPosts()
    }

    private fun getPosts() {
        postRepository.getPosts()
            .onEach { result ->
                val newState = when (result) {
                    is Result.Loading -> {
                        _uiState.value.copy(isLoading = true, error = null)
                    }
                    is Result.Success -> {
                        _uiState.value.copy(isLoading = false, posts = result.data, error = null)
                    }
                    is Result.Error -> {
                        _uiState.value.copy(isLoading = false, error = result.message ?: "An unknown error occurred")
                    }
                }
                _uiState.value = newState // 在新版本的Kotlin中，可以用 _uiState.update { ... }
            }
            .launchIn(viewModelScope)
    }
}