package com.template.data.network.apiservice

import com.template.core.model.SolveQuestionResponse
import com.template.data.network.di.PublicClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolverApiServiceImpl @Inject constructor(
    @PublicClient private val httpClient: HttpClient
) : SolverApiService {

    override suspend fun solveQuestion(imageFile: File): SolveQuestionResponse {
        return httpClient.submitFormWithBinaryData(
            // 以后在这里替换成你的真实 URL
            url = "https://example.com/mock/api/solve_question",
            formData = formData {
                append("image", imageFile.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg") // or "image/png"
                    append(HttpHeaders.ContentDisposition, "filename=\"${imageFile.name}\"")
                })
            }
        ).body()
    }
}