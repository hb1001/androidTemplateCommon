package com.template.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.template.core.model.Post
import com.template.core.model.Reactions
import kotlinx.coroutines.delay

// 假设你的 API 返回这样的数据结构
data class PostsApiResponse(val posts: List<Post>, val total: Int, val skip: Int, val limit: Int)

class PostPagingSource(
) : PagingSource<Int, Post>() {

    private val allPosts = List(100) { index ->
        Post(
            id = index + 1,
            title = "标题 #${index + 1}",
            body = "这是第 ${index + 1} 条模拟内容。",
            tags = listOf("标签1", "标签2"),
            reactions = Reactions(likes = 10, dislikes = 5),
            views = 100,
            userId = index + 1
        )
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        // 计算分页范围
        val fromIndex = page * pageSize
        val toIndex = minOf(fromIndex + pageSize, allPosts.size)

        // 模拟网络延迟
        delay(3000L)

        // 截取当前页数据
        val currentPage = if (fromIndex < allPosts.size) {
            allPosts.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }

        return LoadResult.Page(
            data = currentPage,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (toIndex >= allPosts.size) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        // 当数据刷新时，确定从哪里开始加载
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}