package com.naeggeodo.presentation.di

import com.damda.data.repository.home.HomeRepositoryImpl
import com.damda.data.repository.home.remote.HomeRemoteDataSourceImpl
import com.damda.data.repository.login.LoginRepositoryImpl
import com.damda.data.repository.login.SearchRepositoryImpl
import com.damda.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.naeggeodo.data.repository.chat.ChatRepositoryImpl
import com.naeggeodo.data.repository.chat.remote.ChatRemoteDataSourceImpl
import com.naeggeodo.data.repository.create.CreateRepositoryImpl
import com.naeggeodo.data.repository.create.remote.CreateRemoteDataSourceImpl
import com.naeggeodo.data.repository.search.remote.SearchRemoteDataSourceImpl
import com.naeggeodo.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideLoginRepository(
        loginRemoteDataSourceImpl: LoginRemoteDataSourceImpl
    ): LoginRepository {
        return LoginRepositoryImpl(loginRemoteDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(
        homeRemoteDataSourceImpl: HomeRemoteDataSourceImpl
    ): HomeRepository {
        return HomeRepositoryImpl(homeRemoteDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchRemoteDataSourceImpl: SearchRemoteDataSourceImpl
    ): SearchRepository {
        return SearchRepositoryImpl(searchRemoteDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideCreateRepository(
        createRemoteDataSourceImpl: CreateRemoteDataSourceImpl
    ): CreateRepository {
        return CreateRepositoryImpl(createRemoteDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideChatRepository(
        chatRemoteDataSourceImpl: ChatRemoteDataSourceImpl
    ): ChatRepository {
        return ChatRepositoryImpl(chatRemoteDataSourceImpl)
    }
}