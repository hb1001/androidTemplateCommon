// File: core-model/src/main/java/com/template/core/model/Post.kt
package com.template.core.model

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val posts: List<Post>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val tags: List<String>,
    val reactions: Reactions,
    val views: Int,
    val userId: Int
)

@Serializable
data class Reactions(
    val likes: Int,
    val dislikes: Int
)