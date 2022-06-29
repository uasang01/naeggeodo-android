package com.naeggeodo.presentation.di

import com.naeggeodo.data.api.*
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
    fun provideSearchChatListByCategoryApiService(@Named("NoAuth") retrofit: Retrofit): SearchChatListByCategoryApi {
        return retrofit.create(SearchChatListByCategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetTagsApiService(@Named("Auth") retrofit: Retrofit): GetTagsApi {
        return retrofit.create(GetTagsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchChatListByKeyWordApiService(@Named("Auth") retrofit: Retrofit): SearchChatListByKeyWordApi {
        return retrofit.create(SearchChatListByKeyWordApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGetChatHistoriesByKeyWordApiService(@Named("Auth") retrofit: Retrofit): GetChatHistoriesApi {
        return retrofit.create(GetChatHistoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCreateChatApiService(@Named("Auth") retrofit: Retrofit): CreateChatApi {
        return retrofit.create(CreateChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetChatInfoApiService(@Named("Auth") retrofit: Retrofit): GetChatInfoApi {
        return retrofit.create(GetChatInfoApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGetUsersInChatApiService(@Named("Auth") retrofit: Retrofit): GetUsersInChatApi {
        return retrofit.create(GetUsersInChatApi::class.java)
    }

}