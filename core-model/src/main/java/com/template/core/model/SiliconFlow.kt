package com.template.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Request Models ---

@Serializable
data class SiliconFlowRequest(
    val model: String,
    val messages: List<Message>,
    val stream: Boolean = false,
    @SerialName("max_tokens")
    val maxTokens: Int = 4096,
    val temperature: Double = 0.7,
    @SerialName("top_p")
    val topP: Double = 0.7,
    // 其他参数如果需要可以继续添加
)

@Serializable
data class Message(
    val role: String,
    val content: List<ContentPart>
)

@Serializable
sealed interface ContentPart {
    // 我们可以从接口中移除 'type' 属性，让序列化器完全自动处理
    // val type: String
}

@Serializable
@SerialName("image_url") // 这个 SerialName 将作为辨别器 'type' 字段的值
data class ImageContentPart(
    @SerialName("image_url")
    val imageUrl: ImageUrl
    // 移除了冲突的 'type' 属性
) : ContentPart

@Serializable
@SerialName("text") // 这个 SerialName 将作为辨别器 'type' 字段的值
data class TextContentPart(
    val text: String
    // 移除了冲突的 'type' 属性
) : ContentPart

@Serializable
data class ImageUrl(
    val url: String,
    val detail: String = "auto"
)
// --- Response Models ---

@Serializable
data class SiliconFlowResponse(
    val id: String,
    val choices: List<Choice>,
    val created: Long,
    val model: String,
    @SerialName("object")
    val objectType: String,
    @SerialName("system_fingerprint")
    val systemFingerprint: String?,
    val usage: Usage,
    val error: SiliconFlowError? = null // 用于捕获API层面的错误
)

@Serializable
data class Choice(
    @SerialName("finish_reason")
    val finishReason: String,
    val index: Int,
    val message: ResponseMessage
)

@Serializable
data class ResponseMessage(
    val content: String,
    val role: String
)

@Serializable
data class Usage(
    @SerialName("completion_tokens")
    val completionTokens: Int,
    @SerialName("prompt_tokens")
    val promptTokens: Int,
    @SerialName("total_tokens")
    val totalTokens: Int
)

@Serializable
data class SiliconFlowError(
    val code: String?,
    val message: String
)