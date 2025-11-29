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
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketApiServiceImpl @Inject constructor(
    @param:WsClient private val client: HttpClient // 使用 @WsClient 注入 CIO client
) : WebSocketApiService {

    // 持有当前的 WebSocket 会话
    private var socketSession: WebSocketSession? = null

    override fun initSession(): Flow<SocketMessage> = flow {
        try {
            // 1. 建立连接 (获取 Session)
            // 模拟器访问本机后端需用 10.0.2.2，端口 8080，路径 /chat (对应后端)
            socketSession = client.webSocketSession {
                url("ws://192.168.1.4:8080/chat")
            }
            Timber.tag("WS").d("Connected to server")

            // 2. 监听消息 (Incoming)
            val session = socketSession
            if (session != null) {
                // 只要 session 活跃，就循环读取
                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        Timber.tag("WS").d("Received: $text")

                        // 3. 转换为 Model 发射出去
                        // 这里暂时假设后端只发纯文本，我们封装成 SocketMessage
                        emit(
                            SocketMessage(
                                content = text,
                                isFromMe = false
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag("WS").e(e, "Connection error")
            // 这里可以发射一个特定的错误消息，或者直接结束流
        } finally {
            // Flow 结束时清理资源
            closeSession()
        }
    }

    override suspend fun sendMessage(content: String) {
        val session = socketSession
        if (session != null && session.isActive) {
            try {
                session.send(Frame.Text(content))
                Timber.tag("WS").d("Sent: $content")
            } catch (e: Exception) {
                Timber.tag("WS").e(e, "Send error")
            }
        } else {
            Timber.tag("WS").w("Cannot send message: Session is null or inactive")
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