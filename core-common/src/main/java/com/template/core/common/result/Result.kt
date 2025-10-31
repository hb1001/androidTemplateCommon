// File: core-common/src/main/java/com/template/core/common/result/Result.kt
package com.template.core.common.result

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable? = null, val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>() // 可以添加一个加载中状态
}