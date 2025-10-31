package com.template.data.network.apiservice

import com.template.core.model.SolveQuestionResponse
import io.ktor.client.statement.HttpResponse
import java.io.File

interface SolverApiService {
    /**
     * 上传问题图片并获取答案
     * @param imageFile The image file to be uploaded.
     * @return The response from the server.
     */
    suspend fun solveQuestion(imageFile: File): SolveQuestionResponse
}