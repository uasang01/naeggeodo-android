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
    fun provideCreateChatApiService(@Named("Auth") retrofit: Retrofit): CreateChatApi {
        return retrofit.create(CreateChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRoomApiService(@Named("Auth") retrofit: Retrofit): ChatRoomApi {
        return retrofit.create(ChatRoomApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGetUsersInChatApiService(@Named("Auth") retrofit: Retrofit): GetUsersInChatApi {
        return retrofit.create(GetUsersInChatApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGetPrevChatHistoryApiService(@Named("Auth") retrofit: Retrofit): GetPrevChatHistoryApi {
        return retrofit.create(GetPrevChatHistoryApi::class.java)
    }
    @Provides
    @Singleton
    fun provideGetChatCreationHistoryApiService(@Named("Auth") retrofit: Retrofit): GetChatCreationHistoryApi {
        return retrofit.create(GetChatCreationHistoryApi::class.java)
    }
    @Provides
    @Singleton
    fun provideBookmarkingApiService(@Named("Auth") retrofit: Retrofit): BookmarkingApi {
        return retrofit.create(BookmarkingApi::class.java)
    }
    @Provides
    @Singleton
    fun provideQuickChatApiService(@Named("Auth") retrofit: Retrofit): QuickChatApi {
        return retrofit.create(QuickChatApi::class.java)
    }

}