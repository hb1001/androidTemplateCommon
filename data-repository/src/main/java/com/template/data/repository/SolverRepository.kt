package com.template.data.repository

import com.template.core.common.result.Result
import com.template.data.network.apiservice.SolverApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface SolverRepository {
    fun solveQuestion(imageFile: File): Flow<Result<String>>
}

@Singleton
class SolverRepositoryImpl @Inject constructor(
    private val solverApiService: SolverApiService
) : SolverRepository {
    override fun solveQuestion(imageFile: File): Flow<Result<String>> = flow {
        emit(Result.Loading)

        // 调用新的 API Service
        val response = solverApiService.solveQuestion(imageFile)

        // 从复杂的响应中提取出需要的答案
        val answer = response.choices.firstOrNull()?.message?.content

        if (!answer.isNullOrBlank()) {
            emit(Result.Success(answer))
        } else {
            // 如果 API 返回了错误结构体，使用它的 message
            val errorMessage = response.error?.message ?: "Failed to get a valid answer."
            emit(Result.Error(message = errorMessage))
        }
    }.catch { e ->
        // 捕获网络异常，比如超时、无法连接等
        emit(Result.Error(exception = e, message = "Network request failed: ${e.message}"))
    }
}