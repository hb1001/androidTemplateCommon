package com.template.data.repository

import com.template.core.common.coroutine.IoDispatcher
import com.template.core.common.result.Result
import com.template.data.datastore.TokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MockAuthRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun getAuthToken(): Flow<String?> {
        return tokenManager.getAuthToken()
    }

    override suspend fun login(params: Map<String, Any>): Result<Unit> = withContext(ioDispatcher) {
        // 模拟网络延迟
        delay(500)

        val username = params["username"] as? String
        val password = params["password"] as? String

        // 模拟登录逻辑
        if (username == "admin" && password == "123456") {
            // 模拟登录成功，生成一个假的 token
            val fakeToken = "fake_token_for_${username}_${System.currentTimeMillis()}"
            tokenManager.saveAuthToken(fakeToken)
            Result.Success(Unit)
        } else {
            // 模拟登录失败
            Result.Error(message = "Invalid username or password")
        }
    }

    override suspend fun logout() {
        tokenManager.clearAuthToken()
    }
}