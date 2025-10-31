package com.template.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val startRoute: String) : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = authRepository.getAuthToken()
        .map { token ->
            // 如果 token 不为空且有效 (这里简单判断非空)，则起始页是首页
            if (!token.isNullOrBlank()) {
                MainUiState.Success(com.template.core.navigation.AppRoutes.HOME_ROUTE)
            } else {
                // 否则是登录页
                MainUiState.Success(com.template.core.navigation.AppRoutes.LOGIN_ROUTE)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading // 初始状态为加载中
        )
}