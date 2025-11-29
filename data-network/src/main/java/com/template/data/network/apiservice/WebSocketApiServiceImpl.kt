package com.template.data.network.apiservice

import com.template.core.model.SocketMessage
import com.template.data.network.di.WsClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class WebSocketApiServiceImpl @Inject constructor(
    @param:WsClient private val client: HttpClient
) : WebSocketApiService {

    private var socketSession: WebSocketSession? = null

    // [新增] 模拟设备ID，应用启动期间不变
    private val myDeviceId = java.util.UUID.randomUUID().toString().substring(0, 8)

    override fun initSession(): Flow<SocketMessage> = flow {
        try {
            // [修改] URL 带上 deviceId
            val urlString = "ws://192.168.1.4:8080/chat?deviceId=$myDeviceId"

            socketSession = client.webSocketSession {
                url(urlString)
            }
            Timber.tag("WS").d("Connected with ID: $myDeviceId")

            val session = socketSession
            if (session != null) {
                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()

                        try {
                            // [修改] 解析后端发来的 JSON
                            // 这里的 SocketMessage 需要和后端的 ChatMessage 结构对齐
                            val msg = Json.decodeFromString<SocketMessage>(text)

                            // [关键] 判断消息是否是自己发的
                            // 这里我们利用 copy 方法修正 isFromMe 属性
                            val processedMsg = msg.copy(
                                isFromMe = (msg.senderId == myDeviceId)
                            )

                            emit(processedMsg)
                        } catch (e: Exception) {
                            Timber.e(e, "JSON Parse error: $text")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // ... 同前
        } finally {
            closeSession()
        }
    }

    override suspend fun sendMessage(content: String) {
        // [修改] 发送时只发送纯文本内容，而不是 JSON
        // 这样后端容易处理，后端会把内容包装成 JSON 广播回来
        val session = socketSession
        if (session != null && session.isActive) {
            session.send(Frame.Text(content))
        }
    }

    override suspend fun closeSession() {
        try {
            socketSession?.close(CloseReason(CloseReason.Codes.NORMAL, "Client closed"))
        } catch (e: Exception) {
            Timber.tag("WS").e(e, "Close error")
        } finally {
            socketSession = null
            Timber.tag("WS").d("Session closed")
        }
    }
}