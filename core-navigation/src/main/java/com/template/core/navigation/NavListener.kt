package com.template.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
@Composable
fun <T> NavController.listenResult(
    key: String,
    onResult: (T) -> Unit
) {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle
    val resultFlow = savedStateHandle?.getStateFlow<T?>(key, null)
    val result by resultFlow?.collectAsState() ?: remember { mutableStateOf(null) }

    LaunchedEffect(result) {
        result?.let {
            onResult(it)
            // 使用后清除，避免重复触发
            savedStateHandle?.set(key, null)
        }
    }
}

@Composable
fun NavController.listenResults(
    keys: List<String>,
    onResult: (key: String, value: String) -> Unit
) {
    val savedStateHandle = currentBackStackEntry?.savedStateHandle ?: return

    keys.forEach { key ->
        val flow = savedStateHandle.getStateFlow<String?>(key, null)
        val result by flow.collectAsState()

        LaunchedEffect(result) {
            if (result != null) {
                onResult(key, result!!)
            }
            savedStateHandle[key] = null
        }
    }
}
