package com.naeggeodo.presentation.di

import com.naeggeodo.data.repository.chat.ChatRepositoryImpl
import com.naeggeodo.data.repository.chat.remote.ChatRemoteDataSourceImpl
import com.naeggeodo.data.repository.create.CreateRepositoryImpl
import com.naeggeodo.data.repository.create.remote.CreateRemoteDataSourceImpl
import com.naeggeodo.data.repository.home.HomeRepositoryImpl
import com.naeggeodo.data.repository.home.remote.HomeRemoteDataSourceImpl
import com.naeggeodo.data.repository.info.InfoRepositoryImpl
import com.naeggeodo.data.repository.info.remote.InfoRemoteDataSourceImpl
import com.naeggeodo.data.repository.login.LoginRepositoryImpl
import com.naeggeodo.data.repository.login.SearchRepositoryImpl
import com.naeggeodo.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.naeggeodo.data.repository.remit.RemitRepositoryImpl
import com.naeggeodo.data.repository.remit.remote.RemitRemoteDataSourceImpl
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

    @Singleton
    @Provides
    fun provideInfoRepository(
        infoRemoteDataSourceImpl: InfoRemoteDataSourceImpl
    ): InfoRepository {
        return InfoRepositoryImpl(infoRemoteDataSourceImpl)
    }

    @Singleton
    @Provides
    fun provideRemitRepository(
        remitRemoteDataSourceImpl: RemitRemoteDataSourceImpl
    ): RemitRepository {
        return RemitRepositoryImpl(remitRemoteDataSourceImpl)
    }
}