package com.template.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.template.data.database.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    /**
     * 获取所有帖子的数据流。
     * Room 会自动处理当 'posts' 表数据变化时，这个 Flow 会发射新的列表。
     */
    @Query("SELECT * FROM posts")
    fun getPostsStream(): Flow<List<PostEntity>>

    /**
     * 插入或更新帖子列表。
     * 如果帖子已存在（基于主键），则更新它；否则，插入新帖子。
     */
    @Upsert
    suspend fun upsertPosts(posts: List<PostEntity>)

    /**
     * 删除所有帖子。
     */
    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}