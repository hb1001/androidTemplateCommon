package com.template.data.network.apiservice

import com.template.core.model.SocketMessage // 假设你已经在 core-model 定义了这个
import kotlinx.coroutines.flow.Flow

interface WebSocketApiService {
    /**
     * 初始化连接并开始接收消息
     * @return 返回接收到的消息流
     */
    fun initSession(): Flow<SocketMessage>

    /**
     * 发送消息
     */
    suspend fun sendMessage(content: String)

    /**
     * 断开连接
     */
    suspend fun closeSession()
}