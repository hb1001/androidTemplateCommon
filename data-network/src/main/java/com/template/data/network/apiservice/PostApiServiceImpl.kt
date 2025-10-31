package com.template.data.network.apiservice

import com.template.core.model.PostResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class PostApiServiceImpl @Inject constructor(
    private val httpClient: HttpClient
) : PostApiService {

    override suspend fun getPosts(): PostResponse {
        return httpClient.get("posts").body()
    }
}