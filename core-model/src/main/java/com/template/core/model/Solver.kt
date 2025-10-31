package com.template.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SolveQuestionResponse(
    @SerialName("answer_text")
    val answerText: String?,
    val error: String? = null
)