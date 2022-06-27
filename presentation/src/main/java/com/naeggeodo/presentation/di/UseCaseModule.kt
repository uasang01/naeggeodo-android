package com.naeggeodo.presentation.di

import com.naeggeodo.domain.repository.CreateRepository
import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.repository.SearchRepository
import com.naeggeodo.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun provideLogInUseCase(repository: LoginRepository) = LoginUseCase(repository)

    @Provides
    @Singleton
    fun provideCategoryUseCase(repository: HomeRepository) = CategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTagsUseCase(repository: SearchRepository) = GetTagsUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchChatListByCategoryUseCase(repository: HomeRepository) = SearchChatListByCategoryUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchChatListByKeyWordUseCase(repository: SearchRepository) = SearchChatListByKeyWordUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateChatUseCase(repository: CreateRepository) = CreateChatUseCase(repository)
}
