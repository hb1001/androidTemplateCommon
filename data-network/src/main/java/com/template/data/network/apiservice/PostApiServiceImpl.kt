package com.template.data.network.apiservice

import com.template.core.model.PostResponse
import com.template.data.network.di.PublicClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class PostApiServiceImpl @Inject constructor(
    @param:PublicClient private val httpClient: HttpClient // <-- 使用 @PublicClient
) : PostApiService {
    override suspend fun getPosts(): PostResponse {
        return httpClient.get("posts").body()
    }
}