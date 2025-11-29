package com.template.data.network.di

import com.template.data.datastore.TokenManager
import com.template.data.network.apiservice.PostApiService
import com.template.data.network.apiservice.PostApiServiceImpl
import com.template.data.network.apiservice.SolverApiService
import com.template.data.network.apiservice.SolverApiServiceImpl
import com.template.data.network.apiservice.UserService
import com.template.data.network.apiservice.UserServiceImpl
import com.template.data.network.apiservice.WebSocketApiService
import com.template.data.network.apiservice.WebSocketApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindPostApiService(impl: PostApiServiceImpl): PostApiService

    @Binds
    @Singleton
    abstract fun bindUserService(impl: UserServiceImpl): UserService

    @Binds // <-- 新增
    @Singleton
    abstract fun bindSolverApiService(impl: SolverApiServiceImpl): SolverApiService


    // 新增：绑定 WebSocket Service
    @Binds
    @Singleton
    abstract fun bindWebSocketApiService(impl: WebSocketApiServiceImpl): WebSocketApiService

    companion object {

        // 提炼出一个公共的 JSON 配置，避免重复
        private val AppJson = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }


        // 新增：提供专门的 WebSocket HttpClient (CIO 引擎)
        @Provides
        @Singleton
        @WsClient
        fun provideWebSocketHttpClient(): HttpClient {
            return HttpClient(CIO) {
                // 1. 安装 WebSockets 插件
                install(WebSockets) {
                    // 心跳检测，防止连接假死
                    pingInterval = 20_000 // 20秒
                    maxFrameSize = Long.MAX_VALUE
                }

                // 2. 安装 Logging (调试用)
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("KtorHttpClient-WS").d(message)
                        }
                    }
                }

                // 3. 配置超时
                // WebSocket 是长连接，通常不需要 requestTimeout，或者设为无限
                install(HttpTimeout) {
                    connectTimeoutMillis = 10000 // 连接超时 10秒
                    socketTimeoutMillis = Long.MAX_VALUE // 保持 Socket 不断开
                }
            }
        }


        @Provides
        @Singleton
        @PublicClient
        fun providePublicHttpClient(): HttpClient {
            return HttpClient(OkHttp) {

                install(HttpTimeout) {
                    // 请求总超时，包括连接和读取
                    requestTimeoutMillis = 60000 // 30 秒

                    // 连接超时
                    connectTimeoutMillis = 45000 // 15 秒

                    // Socket (读取) 超时
                    socketTimeoutMillis = 45000 // 15 秒
                }

                // 正确的 install 方式
                install(ContentNegotiation) {
                    json(AppJson)
                }

                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("KtorHttpClient-Public").d(message)
                        }
                    }
                }

                defaultRequest {
                    url("https://dummyjson.com/")
                }
            }
        }

        @Provides
        @Singleton
        @AuthedClient
        fun provideAuthedHttpClient(
            // @PublicClient publicClient: HttpClient, // Mock 刷新时暂时不需要
            tokenManager: TokenManager
        ): HttpClient {
            return HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(AppJson)
                }

                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("KtorHttpClient-Authed").d(message)
                        }
                    }
                }

                defaultRequest {
                    url("https://dummyjson.com/")
                }

                install(Auth) {
                    bearer {
                        loadTokens {
                            val token = tokenManager.getAuthToken().first()
                            if (token != null) {
                                BearerTokens(accessToken = token, refreshToken = "")
                            } else {
                                null
                            }
                        }

                        refreshTokens {
                            Timber.d("Token expired, attempting to refresh...")

                            // ---- Mock 逻辑 ----
                            val oldToken = oldTokens?.accessToken ?: ""
                            if (oldToken.isNotBlank()) {
                                val newToken = "refreshed_token_from_${oldToken.take(16)}_${System.currentTimeMillis()}"
                                tokenManager.saveAuthToken(newToken)
                                Timber.d("Token refreshed successfully: $newToken")
                                BearerTokens(accessToken = newToken, refreshToken = "")
                            } else {
                                Timber.w("No token to refresh, clearing auth.")
                                tokenManager.clearAuthToken()
                                null
                            }
                        }

                        // 注意：dummyjson.com 的 /auth/login 接口不需要 Bearer Token
                        // 所以这里的判断逻辑是正确的
                        sendWithoutRequest { request ->
                            request.url.encodedPath.contains("/auth/login")
                        }
                    }
                }

                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 2)
                    exponentialDelay()
                }
            }
        }
    }
}