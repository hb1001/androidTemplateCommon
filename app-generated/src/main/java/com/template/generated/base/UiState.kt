package com.template.generated.base

import com.template.core.model.Post

data class UiState<T>(
    val data:T,
    val isLoading: Boolean = false,
    val error: String? = null
)