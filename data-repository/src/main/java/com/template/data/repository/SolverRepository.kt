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
        val response = solverApiService.solveQuestion(imageFile)
        if (response.answerText != null) {
            emit(Result.Success(response.answerText!!))
        } else {
            emit(Result.Error(message = response.error ?: "Unknown error"))
        }
    }.catch { e ->
        emit(Result.Error(exception = e, message = "Network request failed"))
    }
}