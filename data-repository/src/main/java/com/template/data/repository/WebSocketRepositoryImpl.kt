package com.template.data.repository

import com.template.core.common.result.Result
import com.template.core.model.SocketMessage
import com.template.data.network.apiservice.WebSocketApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketApiService: WebSocketApiService
) : WebSocketRepository {

    override fun connectAndObserveMessages(): Flow<Result<SocketMessage>> {
        return webSocketApiService.initSession()
            .map { message ->
                // 将接收到的原始数据包装为 Success
                Result.Success(message) as Result<SocketMessage>
            }
            .onStart {
                // 流开始时发射 Loading 状态
                emit(Result.Loading)
                Timber.d("WebSocket connecting...")
            }
            .catch { e ->
                // 捕获异常（例如连接失败、断网）
                Timber.e(e, "WebSocket connection error")
                emit(Result.Error(exception = e, message = "Connection failed"))
            }
    }

    override suspend fun sendMessage(message: String) {
        webSocketApiService.sendMessage(message)
    }

    override suspend fun closeConnection() {
        webSocketApiService.closeSession()
    }
}