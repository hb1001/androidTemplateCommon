package com.template.data.network.apiservice

import com.template.core.common.utils.ImageUtils
import com.template.core.model.*
import com.template.data.network.di.PublicClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolverApiServiceImpl @Inject constructor(
    @param:PublicClient private val httpClient: HttpClient
) : SolverApiService {

    // 从安全的地方获取 Token，这里暂时硬编码
    private val apiToken = "sk-jxnibhhbbfcnjmfrzlpcpasruvorqlmaxujyqzmzabffolid"
    private val prompt = "用中文回答题目的问题。如果是选择题，先给出答案，然后简单解释原因。如果不是选择题，简单回答问题"

    override suspend fun solveQuestion(imageFile: File): SiliconFlowResponse {
        // 1. 将图片文件编码为 Base64 Data URL
        val base64Image = ImageUtils.fileToBase64DataUrl(imageFile)
            ?: throw IllegalStateException("Failed to encode image to Base64")

        // 2. 构建请求体
        val requestBody = SiliconFlowRequest(
            model = "Qwen/Qwen3-VL-32B-Instruct",
            messages = listOf(
                Message(
                    role = "user",
                    content = listOf(
                        ImageContentPart(
                            imageUrl = ImageUrl(url = base64Image)
                        ),
                        TextContentPart(
                            text = prompt
                        )
                    )
                )
            )
        )

        // 3. 发送 POST 请求
        return httpClient.post("https://api.siliconflow.cn/v1/chat/completions") {
            headers {
                append("Authorization", "Bearer $apiToken")
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }.body()
    }
}