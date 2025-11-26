package com.template.data.datastore.di

import com.template.data.datastore.SettingsManager
import com.template.data.datastore.SettingsManagerImpl
import com.template.data.datastore.TokenManager
import com.template.data.datastore.TokenManagerImpl
import com.template.data.datastore.UserStateManager
import com.template.data.datastore.UserStateManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindTokenManager(impl: TokenManagerImpl): TokenManager


    @Binds
    @Singleton
    abstract fun bindUserStateManager(impl: UserStateManagerImpl): UserStateManager


    @Binds
    @Singleton
    abstract fun bindSettingsManager(impl: SettingsManagerImpl): SettingsManager
}