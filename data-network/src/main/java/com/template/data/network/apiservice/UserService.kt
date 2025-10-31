package com.template.data.network.apiservice

import com.template.core.model.UserProfile
import com.template.data.network.di.AuthedClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

interface UserService {
    suspend fun getMyProfile(): UserProfile
}

@Singleton
class UserServiceImpl @Inject constructor(
    @AuthedClient private val httpClient: HttpClient // <-- 使用 @AuthedClient
) : UserService {
    override suspend fun getMyProfile(): UserProfile {
        // dummyjson.com 有一个 /auth/me 接口，我们可以用它来测试
        return httpClient.get("auth/me").body()
    }
}