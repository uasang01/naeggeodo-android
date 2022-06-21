package com.naeggeodo.presentation.di

import com.naeggeodo.data.api.CategoryApi
import com.naeggeodo.data.api.GetChatListApi
import com.naeggeodo.data.api.LogInApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideLogInApiService(@Named("NoAuth") retrofit: Retrofit): LogInApi {
        return retrofit.create(LogInApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApiService(@Named("NoAuth") retrofit: Retrofit): CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetChatListApiService(@Named("Auth") retrofit: Retrofit): GetChatListApi {
        return retrofit.create(GetChatListApi::class.java)
    }
}