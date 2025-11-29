package com.template.data.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthedClient

// 新增：专门用于 WebSocket 的客户端注入标记
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WsClient