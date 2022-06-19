package com.naeggeodo.presentation.di

import com.naeggeodo.domain.repository.HomeRepository
import com.naeggeodo.domain.repository.LoginRepository
import com.naeggeodo.domain.usecase.CategoryUseCase
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

    @Provides
    @Singleton
    fun provideCategoryUseCase(repository: HomeRepository) = CategoryUseCase(repository)
}
