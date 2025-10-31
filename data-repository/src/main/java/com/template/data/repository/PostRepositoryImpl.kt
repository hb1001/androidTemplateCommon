package com.template.data.repository

import com.template.core.common.coroutine.IoDispatcher
import com.template.core.common.result.Result
import com.template.core.model.Post
import com.template.data.database.dao.PostDao
import com.template.data.database.entity.asExternalModel
import com.template.data.database.entity.toEntity
import com.template.data.network.apiservice.PostApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApiService: PostApiService,
    private val postDao: PostDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : PostRepository {

    /**
     * 数据流的唯一来源是数据库。
     * UI层订阅这个Flow，每当数据库中的帖子数据发生变化，UI就会自动更新。
     */
    override fun getPosts(): Flow<List<Post>> {
        return postDao.getPostsStream()
            .map { entities -> entities.map { it.asExternalModel() } }
    }

    /**
     * 负责从网络同步数据到本地数据库。
     * 这个方法应该由ViewModel在适当的时候（例如，初始化、下拉刷新）调用。
     * @return 返回一个Result，表示同步操作是成功还是失败。
     */
    suspend fun syncPosts(): Result<Unit> {
        return withContext(ioDispatcher) {
            try {
                val networkPosts = postApiService.getPosts().posts
                postDao.upsertPosts(networkPosts.map { it.toEntity() })
                Result.Success(Unit)
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync posts")
                Result.Error(e, "Sync failed")
            }
        }
    }
}