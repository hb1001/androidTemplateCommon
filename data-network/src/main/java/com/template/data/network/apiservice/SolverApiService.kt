package com.template.data.network.apiservice

import com.template.core.model.SiliconFlowResponse // <-- 改为新的 Response 模型
import java.io.File

interface SolverApiService {
    suspend fun solveQuestion(imageFile: File): SiliconFlowResponse // <-- 返回类型改变
}