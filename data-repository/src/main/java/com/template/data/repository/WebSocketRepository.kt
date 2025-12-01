package com.template.data.repository

import com.template.core.common.result.Result
import com.template.core.model.SocketMessage
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    /**
     * 连接到 WebSocket 服务并持续接收消息。
     * 返回一个 Flow，包含连接状态、收到的消息等。
     */
    fun connectAndObserveMessages(): Flow<Result<SocketMessage>>

    /**
     * 发送消息给服务器。
     */
    suspend fun sendMessage(message: String)

    /**
     * 主动断开连接（通常 ViewModel 在 onCleared 调用，或者 Flow 结束时自动调用）。
     */
    suspend fun closeConnection()
}