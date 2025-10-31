package com.template.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)