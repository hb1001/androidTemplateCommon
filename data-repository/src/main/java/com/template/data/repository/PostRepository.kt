package com.template.data.repository

import androidx.paging.PagingData
import com.template.core.common.result.Result
import com.template.core.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * 帖子数据仓库接口
 */
interface PostRepository {

    /**
     * 获取帖子列表.
     * 使用 Flow 来持续发射数据更新 (例如，先从缓存发射，再从网络发射).
     * @return 一个 Flow，它会发射 Result<List<Post>>.
     */
    fun getPosts(): Flow<List<Post>>

    // 分页获取
    fun getPostsStream(): Flow<PagingData<Post>>
    suspend fun syncPosts(): Result<Unit>
}