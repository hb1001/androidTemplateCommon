package com.template.feature.home

import com.template.core.model.Post

data class HomeUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String? = null
)