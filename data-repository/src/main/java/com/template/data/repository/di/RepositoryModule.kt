package com.template.data.repository.di

import com.template.data.repository.AuthRepository
import com.template.data.repository.MockAuthRepositoryImpl
import com.template.data.repository.PostRepository
import com.template.data.repository.PostRepositoryImpl
import com.template.data.repository.SolverRepository
import com.template.data.repository.SolverRepositoryImpl
import com.template.data.repository.WebSocketRepository
import com.template.data.repository.WebSocketRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository



    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: MockAuthRepositoryImpl): AuthRepository


    @Binds // <-- 新增
    @Singleton
    abstract fun bindSolverRepository(impl: SolverRepositoryImpl): SolverRepository


    @Binds
    @Singleton
    abstract fun bindWebSocketRepository(impl: WebSocketRepositoryImpl): WebSocketRepository
}