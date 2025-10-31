package com.template.data.network.di

import com.template.core.model.PostResponse // 确保导入 core-model 中的类
import com.template.data.network.apiservice.PostApiService
import com.template.data.network.apiservice.PostApiServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class  NetworkModule {
    @Binds
    @Singleton
    abstract fun bindPostApiService(impl: PostApiServiceImpl): PostApiService

    companion object {
        @Provides
        @Singleton
        fun provideHttpClient(): HttpClient {
            return HttpClient(CIO) {
                // 插件配置

                // 1. 配置日志
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Timber.tag("KtorHttpClient").d(message)
                        }
                    }
                }

                // 2. 配置序列化/反序列化
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true // API返回多余字段时，避免崩溃
                    })
                }

                // 3. 配置默认请求参数
                defaultRequest {
                    url("https://dummyjson.com/")
                }
            }
        }
    }
}