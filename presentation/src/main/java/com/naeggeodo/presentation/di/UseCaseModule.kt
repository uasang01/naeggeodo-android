package com.naeggeodo.presentation.di

import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.usecase.LoginUseCase
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
}