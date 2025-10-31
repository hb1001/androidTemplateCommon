package com.template.data.repository

import com.template.core.common.result.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthToken(): Flow<String?>
    suspend fun login(params: Map<String, Any>): Result<Unit>
    suspend fun logout()
}