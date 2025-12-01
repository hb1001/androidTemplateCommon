package com.template.core.model

import kotlinx.serialization.Serializable

class WebSocket {
}

@Serializable // 加上这个注解，方便转 JSON
data class SocketMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val content: String,
    val senderId: String, // [新增] 发送者的设备 ID
    val timestamp: Long = System.currentTimeMillis(),
    // isFromMe 不需要传给后端，这是前端UI逻辑，可以移出构造函数或者设为 @Transient
    // 为了简单，我们保留它但设为默认值，接收时由前端重算
    val isFromMe: Boolean = false
)