package com.naeggeodo.presentation.di

import com.damda.data.repository.login.LoginRepositoryImpl
import com.damda.data.repository.login.remote.LoginRemoteDataSourceImpl
import com.naeggeodo.domain.repository.LoginRepository
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
}