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
    val topP: Double = 0.7
)

@Serializable
data class Message(
    val role: String,
    val content: List<ContentPart>
)

// --- 修正部分开始 ---

@Serializable
sealed interface ContentPart // 密封接口本身不需要任何属性

@Serializable
@SerialName("image_url") // 这个名字会成为辨别器 "type" 字段的值
data class ImageContentPart(
    @SerialName("image_url")
    val imageUrl: ImageUrl
) : ContentPart

@Serializable
@SerialName("text") // 这个名字会成为辨别器 "type" 字段的值
data class TextContentPart(
    val text: String
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