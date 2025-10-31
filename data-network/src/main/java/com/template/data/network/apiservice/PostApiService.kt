package com.template.data.network.apiservice

import com.template.core.model.PostResponse

interface PostApiService {
    /**
     * 获取帖子列表
     */
    suspend fun getPosts(): PostResponse
}