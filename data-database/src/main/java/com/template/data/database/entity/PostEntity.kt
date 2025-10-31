package com.template.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.template.core.model.Post
import com.template.core.model.Reactions

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val body: String,
    // Room 不直接支持 List<String>，需要类型转换器
    // 但为了简化，我们暂时将其存储为逗号分隔的字符串
    val tags: String,
    @Embedded(prefix = "reactions_") // 嵌套对象的字段名前缀
    val reactions: ReactionsEntity,
    val views: Int,
    val userId: Int
)

// 因为 Reactions 是一个复杂对象，需要一个对应的可嵌入类
data class ReactionsEntity(
    val likes: Int,
    val dislikes: Int
)

// 扩展函数，用于 Post (网络模型) -> PostEntity (数据库模型)
fun Post.toEntity(): PostEntity {
    return PostEntity(
        id = this.id,
        title = this.title,
        body = this.body,
        tags = this.tags.joinToString(","), // 简单转换
        reactions = ReactionsEntity(likes = this.reactions.likes, dislikes = this.reactions.dislikes),
        views = this.views,
        userId = this.userId
    )
}

// 扩展函数，用于 PostEntity -> Post
fun PostEntity.asExternalModel(): Post {
    return Post(
        id = this.id,
        title = this.title,
        body = this.body,
        tags = this.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() },
        reactions = Reactions(likes = this.reactions.likes, dislikes = this.reactions.dislikes),
        views = this.views,
        userId = this.userId
    )
}