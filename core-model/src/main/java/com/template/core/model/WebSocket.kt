package com.template.core.model

class WebSocket {
}

data class SocketMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val isFromMe: Boolean, // true=我发的, false=对方发的
    val timestamp: Long = System.currentTimeMillis()
)