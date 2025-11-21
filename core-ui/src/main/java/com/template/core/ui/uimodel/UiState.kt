package com.template.core.ui.uimodel

data class UiState<T>(
    val data:T,
    val isLoading: Boolean = false,
    val error: String? = null
)